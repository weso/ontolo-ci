package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.OntolociHubImplementation;
import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        public void validationTest() {

            for(TestCase testCase : build.getTestCases()) {
                Validate v = new Validate();
                ResultValidation result = v.validateStrExpected(
                        testCase.getOntology(),
                        testCase.getInstances(),
                        testCase.getSchema(),
                        testCase.getProducedShapeMap(),
                        testCase.getExpectedShapeMap()).unsafeRunSync();

                assertTrue(result.getExpectedShapeMap().toJson().spaces2().length() > 0);
            }

        }



    @Test
    public void metadataTest() {

        for(TestCase testCase : build.getTestCases()) {
            final long initTime = System.nanoTime(); // Init counting execution time.
            validate(testCase);
            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.
            assertTrue(executionTimeNS>0);

        }

    }

    @Test
    public void testCaseResultTest() {

        TestCaseResult currentTestCase = null;
        for(TestCase testCase : build.getTestCases()) {
            currentTestCase = TestCaseResult.from(testCase);
            assertEquals(TestCaseResultStatus.WAITING,currentTestCase.getStatus());

            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            assertEquals(TestCaseResultStatus.EXECUTING,currentTestCase.getStatus());

            validate(testCase);

            currentTestCase.setStatus(TestCaseResultStatus.PASS);
            assertEquals(TestCaseResultStatus.PASS,currentTestCase.getStatus());
        }

    }


    @Test
    public void buildResultTest() {
        BuildResult buildResult =  BuildResult.from(new ArrayList<>());
        final Collection<TestCaseResult> testCaseResults = new ArrayList<>();
        TestCaseResult currentTestCase = null;

        assertEquals(0,buildResult.getTestCaseResults().size());
        assertEquals(0,testCaseResults.toArray().length);

        for(TestCase testCase : build.getTestCases()) {
            currentTestCase = TestCaseResult.from(testCase);
            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            validate(testCase);
            currentTestCase.setStatus(TestCaseResultStatus.PASS);
            testCaseResults.add(TestCaseResult.from(testCase));
        }

        buildResult =  BuildResult.from(testCaseResults);
        assertTrue(buildResult.getTestCaseResults().size()>0);
    }


    private void validate(TestCase testCase){
        Validate v = new Validate();
        ResultValidation result = v.validateStrExpected(
                testCase.getOntology(),
                testCase.getInstances(),
                testCase.getSchema(),
                testCase.getProducedShapeMap(),
                testCase.getExpectedShapeMap()).unsafeRunSync();
    }

}
