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
import org.apache.jena.base.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
        String producedResultVaLidation = "";
        String expectedResultVaLidation = "";


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
                producedResultVaLidation = produced.get(0).getStatus();
                expectedResultVaLidation = expected.get(0).getStatus();

                if(expectedResultVaLidation.equals(producedResultVaLidation)){
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
            final Map<String, String> metadata = new HashMap<>(currentTestCase.getMetadata());
            metadata.put("execution_time", executionTimeFormated);
            metadata.put("validation_status",producedResultVaLidation);
            metadata.put("expected_validation_status",expectedResultVaLidation);
            currentTestCase.setMetadata(metadata);

            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }

        final long stopBuildTime = System.nanoTime(); // Stop counting execution time of the build.
        final long executionBuildTimeNS = stopBuildTime - initBuildTime; // Compute execution time of the build.

        final double buildSeconds = (double)executionBuildTimeNS/ 1_000_000_000;
        String executionBuildTimeFormated = String.format("%f sec",buildSeconds);

        final Map<String, String> buildMetadata = new HashMap<>(build.getMetadata());
        buildMetadata.put("execution_time",executionBuildTimeFormated);
        buildMetadata.put("execution_date", String.valueOf(System.currentTimeMillis()));
        buildMetadata.put("checkTitle",checkTitle);
        buildMetadata.put("checkBody",checkBody);
        buildMetadata.put("buildResult", buildResult);

        build.setMetadata(buildMetadata);
        // Finally return the Build result.
        return BuildResult.from(build.getMetadata(),testCaseResults);
    }


}
