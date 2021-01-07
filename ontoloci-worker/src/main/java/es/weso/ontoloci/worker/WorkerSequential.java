package es.weso.ontoloci.worker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.build.BuildResultStatus;
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
 * The sequential worker
 * @author Pablo Menéndez
 */
public class WorkerSequential implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerSequential.class);

    private long initBuildTime;
    private long stopBuildTime;
    private long initTestTime;
    private long stopTestTime;

    /**
     * Validates all the tests from a build and count the time it takes.
     * Fills the build with metadata.
     * Finally sets the build status result.
     *
     * @param build to allocate in the worker.
     * @return buildRestult with all the validated tests and the metadata
     */
    @Override
    public BuildResult executeBuild(Build build) {
        // 1. Init counting execution time of the build
        startBuildCrono();
        // 2. Validate tests
        final Collection<TestCaseResult> testCaseResults = validateTests(build.getTestCases());
        // 3. Stop counting execution time of the build.
        stopBuildCrono();
        // 4. Resolve build result status
        BuildResultStatus buildResultStatus = resolveBuildStatus(testCaseResults);
        // 5. Get the metadata
        final Map<String, String> metadata = fillBuildMetadata(build,buildResultStatus);
        // 6. Create the build result
        BuildResult buildResult = BuildResult.from(metadata,testCaseResults);
        // 7. Set the build result status
        buildResult.setStatus(buildResultStatus);
        // 8. Finally return the Build result.
        return buildResult;
    }


    /**
     * Validates all the tests from a collection an count the time each test takes.
     * Fills each test with metadata.
     * Finally sets each test status result.
     *
     * @param testCases to be validated
     * @return test case results
     */
    private Collection<TestCaseResult> validateTests(Collection<TestCase> testCases){
        final Collection<TestCaseResult> testCaseResults = new ArrayList<>();
        TestCaseResult currentTestCase = null;
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


    /**
     * Checks each test result from a collection and determines if the build has passed without errors or not.
     * If any of the test results has failed, then the build has failed too.
     *
     * @param testCaseResults test results
     * @return build result status
     */
    private BuildResultStatus resolveBuildStatus(Collection<TestCaseResult> testCaseResults) {
        for(TestCaseResult testCase : testCaseResults) {
            if(testCase.getStatus() == TestCaseResultStatus.FAIL)
                return BuildResultStatus.FAILURE;
        }
        return BuildResultStatus.SUCCESS;
    }

    /**
     * Compares the produced validation results with the expected validation results.
     * If any produced shapeMap status has not the same expected status then the test has failed.
     * The produced and the expected results are added to the test metadata.
     *
     * @param resultValidation  result of the validation
     * @param testCaseResult    test case result
     */
    private void compareResults(ResultValidation resultValidation,TestCaseResult testCaseResult) {
        List<ShapeMapResultValidation> expected = getExpectedResult(resultValidation);
        List<ShapeMapResultValidation> produced = getProducedResult(resultValidation);
        TestCaseResultStatus status = TestCaseResultStatus.PASS;

        for(ShapeMapResultValidation e: expected){
            for(ShapeMapResultValidation p: produced){
                if(!compareShapeMapResult(e,p))
                    status = TestCaseResultStatus.FAIL;
            }
        }
        testCaseResult.setStatus(status);
        testCaseResult.addMetadata("produced",produced.toString());
        testCaseResult.addMetadata("expected",expected.toString());
    }

    /**
     * Compares two shapeMap results.
     * Two shapeMap results are the same if they have the same node and the same status.
     *
     * @param e the first shapeMap result
     * @param p the second shapeMap result
     *
     * @return if the shapeMap results are equals or not
     */
    private boolean compareShapeMapResult(ShapeMapResultValidation e,ShapeMapResultValidation p){
        String expectedNode = e.getNode();
        String expectedStatus = e.getStatus();
        String producedNode = p.getNode();
        String producedStatus = e.getStatus();
        return expectedNode.equals(producedNode) && expectedStatus.equals(producedStatus);
    }


    /**
     * Performs the validation of a specific test case.
     *
     * @param testCase to be validated
     * @return the result of the validation
     */
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

    /**
     * Gets the expected result from a ResultValidation object as a list of shapeMaps result validation.
     *
     * @param resultValidation result of validation
     * @return expected result
     */
    private List<ShapeMapResultValidation> getExpectedResult(ResultValidation resultValidation){
        return getResultFromValidation(resultValidation.getExpectedShapeMap());
    }

    /**
     * Gets the produced result from a ResultValidation object as a list of shapeMaps result validation.
     * The shex validator usually infers more things that we need, so we are only keeping shape map results
     * that are in the expected result too.
     *
     * @param resultValidation result of validation
     * @return produced result
     */
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

    /**
     * Maps a shape map into a list of ShapeMapResultValidation objects
     *
     * @param shapeMap shapeMap
     * @return list of ShapeMapResultValidation objects
     */
    private List<ShapeMapResultValidation> getResultFromValidation(ShapeMap shapeMap){
        ObjectMapper jsonMapper  = new ObjectMapper(new JsonFactory());
        try {
            return Arrays.asList(jsonMapper.readValue(shapeMap.toJson().spaces2(), ShapeMapResultValidation[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Replaces the current test metadata with a new map of metadata that contains the test execution time.
     * @param testCase test case
     */
    private void fillTestMetadata(TestCaseResult testCase){
        final Map<String, String> metadata = new HashMap<>(testCase.getMetadata());
        metadata.put("execution_time", getTestTime());
        testCase.setMetadata(metadata);
    }

    /**
     * Creates a new metadata Map with:
     *
     *      checkTitle      ->  Title for the check run
     *      checkBody       ->  Body msg for the check run
     *      execution_time  ->  Execution time of the build
     *      execution_date  ->  Execution date of the build
     *
     * @param build             build
     * @param buildStatus       status of the build
     * @return  build metadata
     */
    private Map<String, String> fillBuildMetadata(Build build,BuildResultStatus buildStatus){
        final Map<String, String> buildMetadata = new HashMap<>(build.getMetadata());
        String checkTitle = "Build Passing";
        String checkBody = "All the tests has passed without problems";

        if(buildStatus == BuildResultStatus.FAILURE){
          checkTitle = "Build Passing";
          checkBody = "All the tests has passed without problems";
        }

        buildMetadata.put("checkTitle",checkTitle);
        buildMetadata.put("checkBody",checkBody);
        buildMetadata.put("execution_time",getBuildTime());
        buildMetadata.put("execution_date", String.valueOf(System.currentTimeMillis()));
        return buildMetadata;
    }


    /******  CRONO *******/


    /**
     * Initializes  build crono
     */
    private void startBuildCrono(){
        initBuildTime = System.nanoTime();
    }

    /**
     * Stops build crono
     */
    private void stopBuildCrono(){
        stopBuildTime = System.nanoTime();
    }

    /**
     * Initializes test crono
     */
    private void startTestCrono(){
        initTestTime = System.nanoTime();
    }

    /**
     * Stops test crono
     */
    private void stopTestCrono(){
        stopTestTime = System.nanoTime();
    }


    /**
     * Gets the build execution time in seconds.
     * @return execution time
     */
    private String getBuildTime(){
        return getExecTimeFormated(initBuildTime,stopBuildTime);
    }

    /**
     * Gets the test execution time in seconds.
     * @return execution time
     */
    private String getTestTime(){
        return getExecTimeFormated(initTestTime,stopTestTime);
    }

    /**
     * Calculate the elapsed time between two times in seconds.
     *
     * @param initTime initial time
     * @param stopTime final time
     * @return elapsed time in seconds
     */
    private String getExecTimeFormated(long initTime,long stopTime){
        final long executionTimeNS = stopTime - initTime;
        final double seconds = (double)executionTimeNS/ 1_000_000_000;
        return String.format("%f sec",seconds);
    }

}
