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

import java.util.*;
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
                    LOGGER.debug("PASS ");
                    currentTestCase.setStatus(TestCaseResultStatus.PASS);
                }else{
                    LOGGER.debug("FAIL ");
                    currentTestCase.setStatus(TestCaseResultStatus.FAIL);
                }

            }catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.

            // Set execution time as metadata.
            final Map<String, String> metadata = new HashMap<>(currentTestCase.getMetadata());
            metadata.put("execution_time", Long.toString(executionTimeNS));
            currentTestCase.setMetadata(metadata);

            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }

        // Finally return the Build result.
        return BuildResult.from(build.getMetadata(),testCaseResults);
    }


}
