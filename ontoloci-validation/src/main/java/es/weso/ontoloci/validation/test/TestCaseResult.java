package es.weso.ontoloci.validation.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Models the result of a test execution. The result of a test execution is composed of the tests case that was
 * executed, the result of the execution and a metadata map that can store execution time or other things like
 * profiling if enabled.
 *
 * @author Guillermo Facundo Colunga
 * @version 20201007
 */
public class TestCaseResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCaseResultStatus.class);

    // Final fields
    private final TestCase testCase;
    private final Map<String, String> metadata;

    // Non final fields.
    private TestCaseResultStatus status;

    /**
     * Constructor for test case results where need to set the test case, the initial status and the metadata map.
     *
     * @param testCase that has been executed.
     * @param status of the test case execution.
     * @param metadata is a map with the test execution metadata.
     */
    public TestCaseResult(TestCase testCase, TestCaseResultStatus status, Map<String, String> metadata) {
        this.testCase = testCase;
        this.status = status;
        this.metadata = metadata;

        LOGGER.debug("Creating new test case result for " + this.toString());
    }

    /**
     * Simple constructor that sets default values for the test result status and the metadata. The default value of the
     * status is WAITING and for the metadata just an empty hash map.
     *
     * @param testCase that is linked to this result test case.
     */
    public TestCaseResult(TestCase testCase) {
        this(testCase, TestCaseResultStatus.WAITING, new HashMap<>());

        LOGGER.debug("Creating new test case result for " + this.toString());
    }

    /**
     * Gets the test case linked with this result test case.
     *
     * @return the test case linked with this result test case.
     */
    public TestCase getTestCase() {
        LOGGER.debug("Getting the value of the test case " + this.testCase.toString()
        + " from " + this);

        return this.testCase;
    }

    /**
     * Gets the status of the execution of the linked tests case.
     *
     * @return the status of the execution of the linked tests case.
     */
    public TestCaseResultStatus getStatus() {
        LOGGER.debug("Getting the value of the test case result status " + this.status.toString()
                + " from " + this);

        return status;
    }

    /**
     * Sets the status of the result test case.
     *
     * @param status to set.
     */
    public void setStatus(TestCaseResultStatus status) {
        LOGGER.debug("Setting the value of the test case result status from " + this.status.toString() + " to "
                + status.toString()
                + " from " + this);

        this.status = status;
    }

    /**
     * Gets the metadata map associated to the result test case.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result " + this.metadata.toString()
                + " from " + this);

        return metadata;
    }

    @Override
    public String toString() {
        return "TestCaseResult{" +
                    "testCase=" + testCase +
                    ", metadata=" + metadata +
                    ", status=" + status +
                '}';
    }
}
