package es.weso.ontoloci.worker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.ontoloci.hub.OntolociHubImplementation;
import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.ShapeMapResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class WorkerTest {

        // Repository data example
        private final static String owner = "mistermboy";
        private final static String repo = "oci-test";
        private final static String branch = "main";

        private static Build build;

        @BeforeAll
        public static void setUp(){
            build = Build.from(new ArrayList<>());
            Map<String,String> metadataExample = new HashMap<>();
            metadataExample.put("owner",owner);
            metadataExample.put("repo",repo);
            metadataExample.put("branch",branch);
            build.setMetadata(metadataExample);

            OntolociHubImplementation ontolocyHub = new OntolociHubImplementation();
            //Transform the current build to a HubBuild
            HubBuild hubBuild = build.toHubBuild();
            //Add the tests
            hubBuild = ontolocyHub.addTestsToBuild(hubBuild);
            //Transform the returned HubBuild to a Build and overwrites the result
            build = build.from(hubBuild);
        }

        @Test
        public void validationTest() throws JsonProcessingException {

            for(TestCase testCase : build.getTestCases()) {
                ResultValidation result = validate(testCase);

                assertTrue(result.getExpectedShapeMap().toJson().spaces2().length() > 0);

                List<ShapeMapResultValidation> produced = getProducedShapeMap(result);
                List<ShapeMapResultValidation> expected = getExpectedShapeMap(result);

                assertTrue(expected.get(0).equals(produced.get(0)));
            }
        }



    @Test
    public void metadataTest() {

        for(TestCase testCase : build.getTestCases()) {
            final long initTime = System.nanoTime(); // Init counting execution time.
            validate(testCase);
            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.

            final long minutes = TimeUnit.NANOSECONDS.toMinutes(executionTimeNS);
            final long seconds = TimeUnit.NANOSECONDS.toSeconds(executionTimeNS) -  TimeUnit.NANOSECONDS.toSeconds(minutes);
            final long millis =  TimeUnit.NANOSECONDS.toMillis(executionTimeNS)  -  TimeUnit.NANOSECONDS.toMillis(seconds);
            final float finalSects = Float.parseFloat(seconds+"."+millis);
            DecimalFormat df = new DecimalFormat("##.##");
            System.out.println(df.format(finalSects));

            String executionTimeFormated = String.format("%2d min, %2d sec",minutes,finalSects);

            assertTrue(executionTimeNS>0);
        }
    }

    @Test
    public void testCaseResultPassedTest() {

        TestCaseResult currentTestCase = null;
        for(TestCase testCase : build.getTestCases()) {
            currentTestCase = TestCaseResult.from(testCase);
            assertEquals(TestCaseResultStatus.WAITING,currentTestCase.getStatus());

            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            assertEquals(TestCaseResultStatus.EXECUTING,currentTestCase.getStatus());

            ResultValidation result = validate(testCase);
            List<ShapeMapResultValidation> produced = getProducedShapeMap(result);
            List<ShapeMapResultValidation> expected = getExpectedShapeMap(result);

            assertTrue(expected.get(0).equals(produced.get(0)));

            currentTestCase.setStatus(TestCaseResultStatus.PASS);
            assertEquals(TestCaseResultStatus.PASS,currentTestCase.getStatus());
        }

    }

    @Test
    public void testCaseResultFailedTest() {

        TestCaseResult currentTestCase = null;
        for(TestCase testCase : build.getTestCases()) {
            currentTestCase = TestCaseResult.from(testCase);
            assertEquals(TestCaseResultStatus.WAITING,currentTestCase.getStatus());

            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            assertEquals(TestCaseResultStatus.EXECUTING,currentTestCase.getStatus());

            ResultValidation result = validate(testCase);
            List<ShapeMapResultValidation> produced = getProducedShapeMap(result);
            List<ShapeMapResultValidation> expected = getExpectedShapeMap(result);
            expected.set(0,new ShapeMapResultValidation("","","","",""));

            assertFalse(expected.get(0).equals(produced.get(0)));

            currentTestCase.setStatus(TestCaseResultStatus.FAIL);
            assertEquals(TestCaseResultStatus.FAIL,currentTestCase.getStatus());
        }

    }


    @Test
    public void buildResultTest() {
        BuildResult buildResult =  BuildResult.from(new HashMap<>(),new ArrayList<>());
        final Collection<TestCaseResult> testCaseResults = new ArrayList<>();
        TestCaseResult currentTestCase = null;

        assertEquals(0,buildResult.getTestCaseResults().size());
        assertEquals(0,testCaseResults.toArray().length);

        for(TestCase testCase : build.getTestCases()) {
            currentTestCase = TestCaseResult.from(testCase);
            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            ResultValidation result = validate(testCase);
            List<ShapeMapResultValidation> produced = getProducedShapeMap(result);
            List<ShapeMapResultValidation> expected = getExpectedShapeMap(result);
            if(expected.get(0).equals(produced.get(0))){
                currentTestCase.setStatus(TestCaseResultStatus.PASS);
            }else{
                currentTestCase.setStatus(TestCaseResultStatus.FAIL);
            }
            testCaseResults.add(TestCaseResult.from(testCase));
        }

        buildResult =  BuildResult.from(new HashMap<>(),testCaseResults);
        assertTrue(buildResult.getTestCaseResults().size()>0);
    }


    private ResultValidation validate(TestCase testCase){
        Validate v = new Validate();
        return  v.validateStrExpected(
                testCase.getOntology(),
                testCase.getInstances(),
                testCase.getSchema(),
                testCase.getProducedShapeMap(),
                testCase.getExpectedShapeMap()).unsafeRunSync();
    }

    private List<ShapeMapResultValidation> getProducedShapeMap(ResultValidation result){
        ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
        try {
            return Arrays.asList(
                    jsonMapper.readValue(result.getResultShapeMap().toJson().spaces2(), ShapeMapResultValidation[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ShapeMapResultValidation> getExpectedShapeMap(ResultValidation result){
        ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
        try {
            return Arrays.asList(
                    jsonMapper.readValue(result.getExpectedShapeMap().toJson().spaces2(), ShapeMapResultValidation[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
