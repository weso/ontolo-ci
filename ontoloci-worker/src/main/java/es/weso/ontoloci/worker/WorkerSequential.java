package es.weso.ontoloci.worker;

import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            // For u peibol. Find the validate class in the validation package.
            // Paste here the validation code from Labra, and do not show it to me please...

            Validate v = new Validate();
            /*ResultValidation result = v.validateStrExpected(
                    testCase.getOntology(),
                    testCase.getInstances(),
                    testCase.getSchema(),
                    testCase.getProducedShapeMap(),
                    testCase.getExpectedShapeMap()).unsafeRunSync();*/


            final long stopTime = System.nanoTime(); // Stop counting execution time.

            final long executionTimeNS = stopTime - initTime; // Compute execution time.

            // Set execution time as metadata.
            final Map<String, String> metadata = new HashMap<>(currentTestCase.getMetadata());
            metadata.put("execution_time", Long.toString(executionTimeNS));
            currentTestCase.setMetadata(metadata);

            // 3. If the status is possitive then...
            currentTestCase.setStatus(TestCaseResultStatus.PASS);

            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }

        // Finally return the Build result.
        return BuildResult.from(testCaseResults);
    }
}
