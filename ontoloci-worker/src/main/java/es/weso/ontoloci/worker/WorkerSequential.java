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
        // Temp variable to store each result validation test
        String resultVaLidation = "";

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
                if(expected.get(0).equals(produced.get(0))){
                    currentTestCase.setStatus(TestCaseResultStatus.PASS);
                }else{
                    buildResult = "fail";
                    currentTestCase.setStatus(TestCaseResultStatus.FAIL);
                }

                resultVaLidation = expected.get(0).getStatus();

            }catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.
            final long seconds = TimeUnit.NANOSECONDS.toSeconds(executionTimeNS);
            final long millis =  TimeUnit.NANOSECONDS.toMillis(executionTimeNS)  -  TimeUnit.NANOSECONDS.toMillis(seconds);
            String executionTimeFormated = String.format("%2d,%2d sec",seconds,millis);


            // Set execution time as metadata.
            final Map<String, String> metadata = new HashMap<>(currentTestCase.getMetadata());
            metadata.put("execution_time", executionTimeFormated);
            metadata.put("validation_status",resultVaLidation);
            currentTestCase.setMetadata(metadata);

            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }

        final long stopBuildTime = System.nanoTime(); // Stop counting execution time of the build.
        final long executionBuildTimeNS = stopBuildTime - initBuildTime; // Compute execution time of the build.
        final Map<String, String> buildMetadata = new HashMap<>(build.getMetadata());
        buildMetadata.put("execution_time",String.valueOf(executionBuildTimeNS));
        buildMetadata.put("execution_date", String.valueOf(System.currentTimeMillis()));
        buildMetadata.put("buildResult", buildResult);
        build.setMetadata(buildMetadata);
        // Finally return the Build result.
        return BuildResult.from(build.getMetadata(),testCaseResults);
    }


}
