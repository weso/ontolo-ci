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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The sequantial worker
 */
public class WorkerSequential implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerSequential.class);

    @Override
    public BuildResult executeBuild(Build build) {

        // Temporary array of test results
        final Collection<TestCaseResult> testCaseResults = new ArrayList<>();

        // Temp variable to store each test result.
        TestCaseResult currentTestCase = null;
        // Temp variable to store if the build passes or not (PASS by default)
        String buildResult = "PASS";
        // Temp variables to store the check run title and the check run body
        String checkTitle = "Build Passing";
        String checkBody = "All the tests has passed without problems";
        // Temps variables to store each result validation test
        String producedResultValidation = "";
        String expectedResultVaLidation = "";

        final List<NodeValidation> nodeValidation = new ArrayList<>();

        final long initBuildTime = System.nanoTime(); // Init counting execution time of the build

        for(TestCase testCase : build.getTestCases()) {
            // 1. Create the result object.
            currentTestCase = TestCaseResult.from(testCase);

            // 2. Validate the test case...
            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);

            final long initTime = System.nanoTime(); // Init counting execution time.

            Validate v = new Validate();
            ResultValidation resultValidation = v.validateStrExpected(
                    testCase.getOntology(),
                    testCase.getInstances(),
                    testCase.getSchema(),
                    testCase.getProducedShapeMap(),
                    testCase.getExpectedShapeMap()).unsafeRunSync();

            try {

                ObjectMapper jsonMapper  = new ObjectMapper(new JsonFactory());
                List<ShapeMapResultValidation> produced = Arrays.asList(jsonMapper.readValue(resultValidation.getResultShapeMap().toJson().spaces2(), ShapeMapResultValidation[].class));
                List<ShapeMapResultValidation> expected = Arrays.asList(jsonMapper.readValue(resultValidation.getExpectedShapeMap().toJson().spaces2(), ShapeMapResultValidation[].class));

                TestCaseResult finalCurrentTestCase = currentTestCase;
                producedResultValidation = produced.get(0).getStatus();
                expectedResultVaLidation = expected.get(0).getStatus();

                boolean valid = true;
                for(ShapeMapResultValidation e: expected){
                    for(ShapeMapResultValidation p: produced){
                        if(e.getNode().equals(p.getNode())){
                            nodeValidation.add(new NodeValidation(e.getNode(),p.getStatus(),e.getStatus()));
                            if(!e.getStatus().equals(p.getStatus())){
                                valid = false;
                            }
                        }
                    }
                }

                if(valid){
                    currentTestCase.setStatus(TestCaseResultStatus.PASS);
                }else{
                    buildResult = "fail";
                    checkTitle = "Build Failed";
                    checkBody = "Some test have not passed...";
                    currentTestCase.setStatus(TestCaseResultStatus.FAIL);
                }


            }catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.
            final double seconds = (double)executionTimeNS/ 1_000_000_000;
            String executionTimeFormated = String.format("%f sec",seconds);

            // Set execution time as metadata.
            final Map<String, String> metadata = fillTestMetadata(currentTestCase,executionTimeFormated,producedResultValidation,expectedResultVaLidation);
            currentTestCase.setMetadata(metadata);
            currentTestCase.setNodes(nodeValidation);

            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }

        final long stopBuildTime = System.nanoTime(); // Stop counting execution time of the build.
        final long executionBuildTimeNS = stopBuildTime - initBuildTime; // Compute execution time of the build.

        final double buildSeconds = (double)executionBuildTimeNS/ 1_000_000_000;
        String executionBuildTimeFormated = String.format("%f sec",buildSeconds);

        final Map<String, String> buildMetadata = fillBuildMetadata(build,executionBuildTimeFormated,checkTitle,checkBody,buildResult);
        build.setMetadata(buildMetadata);
        // Finally return the Build result.
        return BuildResult.from(build.getMetadata(),testCaseResults);
    }

    private void validateTests(Collection<TestCase> testCases){
        // Temp variable to store each test result.
        TestCaseResult currentTestCase = null;

        for(TestCase testCase : testCases) {
            // 1. Create the result object.
            currentTestCase = TestCaseResult.from(testCase);
            // 2. Set the status to executing
            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);

            // 3. Init counting execution time.
            final long initTime = System.nanoTime();

            // 4. Validate the test case
            Validate v = new Validate();
            ResultValidation resultValidation = v.validateStrExpected(
                    testCase.getOntology(),
                    testCase.getInstances(),
                    testCase.getSchema(),
                    testCase.getProducedShapeMap(),
                    testCase.getExpectedShapeMap()).unsafeRunSync();



            final List<ShapeMapResultValidation> expected = getExpectedResultFromValidation(resultValidation);
            List<ShapeMapResultValidation> produced = getProducedResultFromValidation(resultValidation);

            produced = compareResults(produced,expected,currentTestCase);


            if(currentTestCase.getStatus() == TestCaseResultStatus.FAIL){
                currentTestCase.setStatus(TestCaseResultStatus.PASS);
                fillPassingBuildMetadata("PASS","Build Passing","All the tests has passed without problems");
            }else{
                currentTestCase.setStatus(TestCaseResultStatus.FAIL);
                fillFaillingBuildMetadata("FAIL","Build Failed","Some test have not passed...");
            }


            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.
            final double seconds = (double)executionTimeNS/ 1_000_000_000;
            String executionTimeFormated = String.format("%f sec",seconds);


            //fillBuildMetadata(currentTestCase.getStatus());

            // Set execution time as metadata.
            //final Map<String, String> metadata = fillTestMetadata(currentTestCase,executionTimeFormated,produced,expected);
            currentTestCase.setMetadata(metadata);
            currentTestCase.setNodes(nodeValidation);

            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }
    }

    private List<ShapeMapResultValidation> getExpectedResultFromValidation(ResultValidation resultValidation){
        return getResultFromValidation(resultValidation.getExpectedShapeMap());
    }

    private List<ShapeMapResultValidation> getProducedResultFromValidation(ResultValidation resultValidation){
        return getResultFromValidation(resultValidation.getResultShapeMap());
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


    private List<ShapeMapResultValidation> compareResults(List<ShapeMapResultValidation> expected,List<ShapeMapResultValidation> produced,TestCaseResult currentTestCase){
        final List<ShapeMapResultValidation> cleanProduced = new ArrayList<>();
        TestCaseResultStatus status = TestCaseResultStatus.PASS;
        for(ShapeMapResultValidation e: expected){
            for(ShapeMapResultValidation p: produced){
                if(e.getNode().equals(p.getNode())){
                    cleanProduced.add(new ShapeMapResultValidation(e.getNode(),e.getShape(),e.getStatus(),e.getAppInfo(),e.getReason()));
                    if(!e.getStatus().equals(p.getStatus())){
                        status = TestCaseResultStatus.FAIL;
                    }
                }
            }
        }
        currentTestCase.setStatus(status);
        return cleanProduced;
    }



    private Map<String, String> fillTestMetadata(TestCaseResult testCase,String executionTimeFormated){
        final Map<String, String> metadata = new HashMap<>(testCase.getMetadata());
        metadata.put("execution_time", executionTimeFormated);
        // metadata.put("validation_status",producedResultValidation);
        // metadata.put("expected_validation_status",expectedResultVaLidation);
        return metadata;
    }

    private Map<String, String> fillBuildMetadata(Build build,String executionBuildTimeFormated,String checkTitle,String checkBody,String buildResult){
        final Map<String, String> buildMetadata = new HashMap<>(build.getMetadata());
        buildMetadata.put("execution_time",executionBuildTimeFormated);
        buildMetadata.put("execution_date", String.valueOf(System.currentTimeMillis()));
        buildMetadata.put("checkTitle",checkTitle);
        buildMetadata.put("checkBody",checkBody);
        buildMetadata.put("buildResult", buildResult);
        return buildMetadata;
    }


}
