package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkerTest {

        // Repository data example
        private final String owner = "mistermboy";
        private final String repo = "oci-test";
        private final String branch = "main";

        @Test
        public void validationTest() {

            GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
            Collection<TestCase> testCases = gitHubService.getTestCases(owner,repo,branch);
            Build build = Build.from(testCases);

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
                ResultValidation result = v.validateStrExpected(
                        testCase.getOntology(),
                        testCase.getInstances(),
                        testCase.getSchema(),
                        testCase.getProducedShapeMap(),
                        testCase.getExpectedShapeMap()).unsafeRunSync();



                final long stopTime = System.nanoTime(); // Stop counting execution time.

                final long executionTimeNS = stopTime - initTime; // Compute execution time.

                // Set execution time as metadata.
                final Map<String, String> metadata = currentTestCase.getMetadata();
                metadata.put("execution_time", Long.toString(executionTimeNS));
                currentTestCase.setMetadata(metadata);

                // 3. If the status is possitive then...
                currentTestCase.setStatus(TestCaseResultStatus.PASS);

                // And finally add it to the collection of results.
                testCaseResults.add(TestCaseResult.from(testCase));
            }

        }

}
