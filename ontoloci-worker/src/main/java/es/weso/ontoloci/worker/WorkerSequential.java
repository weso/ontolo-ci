package es.weso.ontoloci.worker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.ShapeMapResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import es.weso.shapeMaps.ShapeMap;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The sequantial worker
 */
public class WorkerSequential implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerSequential.class);

    private String buildStatus;
    private long initBuildTime;
    private long stopBuildTime;
    private long initTestTime;
    private long stopTestTime;

    @Override
    public BuildResult executeBuild(Build build) {

        startBuildCrono(); // Init counting execution time of the build
        buildStatus = "passing";
        final Collection<TestCaseResult> testCaseResults = validateTests(build.getTestCases());
        stopBuildCrono(); // Stop counting execution time of the build.


            /*if(currentTestCase.getStatus() == TestCaseResultStatus.FAIL){
                currentTestCase.setStatus(TestCaseResultStatus.PASS);
                fillPassingBuildMetadata("PASS","Build Passing","All the tests has passed without problems");
            }else{
                currentTestCase.setStatus(TestCaseResultStatus.FAIL);
                fillFaillingBuildMetadata("FAIL","Build Failed","Some test have not passed...");
            }*/

        build.setMetadata(fillBuildMetadata(build));
        // Finally return the Build result.
        return BuildResult.from(build.getMetadata(),testCaseResults);
    }

    private Collection<TestCaseResult> validateTests(Collection<TestCase> testCases){
        // Temp variable to store each test result.
        TestCaseResult currentTestCase = null;
        final Collection<TestCaseResult> testCaseResults = new ArrayList<>();
        for(TestCase testCase : testCases) {
            // 1. Create the result object.
            currentTestCase = TestCaseResult.from(testCase);
            // 2. Set the status to executing
            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            // 3. Init counting execution time.
            startTestCrono();
            // 4. Validate the test case
            ResultValidation resultValidation = validateTest(testCase);
            // 5. Compare results
            compareResults(resultValidation,currentTestCase);
            // 6. Stop counting execution time.
            stopTestCrono();
            // 7. Add the metadata
            fillTestMetadata(currentTestCase);
            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }
        return testCaseResults;
    }

    private void compareResults(ResultValidation resultValidation,TestCaseResult testCaseResult) {
        List<ShapeMapResultValidation> expected = getExpectedResult(resultValidation);
        List<ShapeMapResultValidation> produced = getProducedResult(resultValidation);
        TestCaseResultStatus status = TestCaseResultStatus.PASS;

        for(ShapeMapResultValidation e: expected){
            for(ShapeMapResultValidation p: produced){
                if(!compare(e,p)) {
                    status = TestCaseResultStatus.FAIL;
                    buildStatus = "failing";
                }
            }
        }
        testCaseResult.setStatus(status);
        testCaseResult.addMetadata("produced",produced.toString());
        testCaseResult.addMetadata("expected",expected.toString());
    }

    private boolean compare(ShapeMapResultValidation e,ShapeMapResultValidation p){
        String expectedNode = e.getNode();
        String expectedStatus = e.getStatus();
        String producedNode = p.getNode();
        String producedStatus = e.getStatus();
        return expectedNode.equals(producedNode) && expectedStatus.equals(producedStatus);
    }


    private ResultValidation validateTest(TestCase testCase){
        Validate v = new Validate();
        ResultValidation resultValidation = v.validateStrExpected(
                testCase.getOntology(),
                testCase.getInstances(),
                testCase.getSchema(),
                testCase.getProducedShapeMap(),
                testCase.getExpectedShapeMap()).unsafeRunSync();
        return resultValidation;
    }

    private List<ShapeMapResultValidation> getExpectedResult(ResultValidation resultValidation){
        return getResultFromValidation(resultValidation.getExpectedShapeMap());
    }

    private List<ShapeMapResultValidation> getProducedResult(ResultValidation resultValidation){
        final List<ShapeMapResultValidation> expected = getResultFromValidation(resultValidation.getExpectedShapeMap());
        final List<ShapeMapResultValidation> produced = getResultFromValidation(resultValidation.getResultShapeMap());
        final List<ShapeMapResultValidation> cleanProduced = new ArrayList<>();

        for(ShapeMapResultValidation e: expected){
            for(ShapeMapResultValidation p: produced){
                if(e.getNode().equals(p.getNode()))
                    cleanProduced.add(new ShapeMapResultValidation(e.getNode(),e.getShape(),e.getStatus(),e.getAppInfo(),e.getReason()));
            }
        }
        return cleanProduced;
    }


    private List<ShapeMapResultValidation> getResultFromValidation(ShapeMap shapeMap){
        ObjectMapper jsonMapper  = new ObjectMapper(new JsonFactory());
        try {
            return Arrays.asList(jsonMapper.readValue(shapeMap.toJson().spaces2(), ShapeMapResultValidation[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void fillTestMetadata(TestCaseResult testCase){
        final Map<String, String> metadata = new HashMap<>(testCase.getMetadata());
        metadata.put("execution_time", getTestTime());
        // metadata.put("validation_status",producedResultValidation);
        // metadata.put("expected_validation_status",expectedResultVaLidation);
        testCase.setMetadata(metadata);
    }

    private Map<String, String> fillBuildMetadata(Build build){
        final Map<String, String> buildMetadata = new HashMap<>(build.getMetadata());
        buildMetadata.put("execution_time",getBuildTime());
        buildMetadata.put("execution_date", String.valueOf(System.currentTimeMillis()));
       // buildMetadata.put("checkTitle",checkTitle);
//        buildMetadata.put("checkBody",checkBody);
//        buildMetadata.put("buildResult", buildResult);
        return buildMetadata;
    }



    private void startBuildCrono(){
        initBuildTime = System.nanoTime();
    }

    private void stopBuildCrono(){
        stopBuildTime = System.nanoTime();
    }

    private void startTestCrono(){
        initTestTime = System.nanoTime();
    }

    private void stopTestCrono(){
        stopTestTime = System.nanoTime();
    }


    private String getExecTimeFormated(long initTime,long stopTime){
        final long executionTimeNS = stopTime - initTime;
        final double seconds = (double)executionTimeNS/ 1_000_000_000;
        return String.format("%f sec",seconds);
    }


    private String getBuildTime(){
        return getExecTimeFormated(initBuildTime,stopBuildTime);
    }

    private String getTestTime(){
        return getExecTimeFormated(initTestTime,stopTestTime);
    }

}
