package es.weso.ontoloci.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
public class PersistedTestCaseResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistedTestCaseResult.class);

    // Final fields
    private final PersistedTestCase testCase;

    // Non final fields.
    private PersistedTestCaseResultStatus status;
    private Map<String, String> metadata;

    /**
     * Factory method to create new instances of the test case result from the test case.
     * It initializes the test case result to the status waiting and empty metadata map.
     *
     * @param testCase from which to create the test case result.
     * @return the new instance of the rest case result.
     */
    public static PersistedTestCaseResult from(PersistedTestCase testCase) {
        LOGGER.debug(
                String.format(
                    "NEW creating a new PersistedTestCaseResult from the static factory for PersistedTestCase with name=[%s], status=[%s] and an empty HashMap<String, String> for the metadata.",
                    testCase.getName(),
                    PersistedTestCaseResultStatus.WAITING
                )
        );

        return new PersistedTestCaseResult(testCase, PersistedTestCaseResultStatus.WAITING, new HashMap<>());
    }

    /**
     * Private constructor for test case results where need to set the test case,
     * the initial status and the metadata map.
     *
     * @param testCase that has been executed.
     * @param status of the test case execution.
     * @param metadata is a map with the test execution metadata.
     */
    private PersistedTestCaseResult(PersistedTestCase testCase, PersistedTestCaseResultStatus status, Map<String, String> metadata) {
        this.testCase = testCase;
        this.status = status;
        this.metadata = metadata;

        LOGGER.debug(
                "Creating a new PersistedTestCaseResult from the static factory for test with name=[%s], status=[%s] and metadata zise=[%s]",
                testCase.getName(),
                this.status,
                this.metadata.size()
        );
    }

    /**
     * Gets the test case linked with this result test case.
     *
     * @return the test case linked with this result test case.
     */
    public PersistedTestCase getTestCase() {
        LOGGER.debug("Getting the value of the test case");

        return this.testCase;
    }

    /**
     * Gets the status of the execution of the linked tests case.
     *
     * @return the status of the execution of the linked tests case.
     */
    public PersistedTestCaseResultStatus getStatus() {
        LOGGER.debug("Getting the value of the test case result status ");

        return status;
    }

    /**
     * Sets the status of the result test case.
     *
     * @param status to set.
     */
    public void setStatus(PersistedTestCaseResultStatus status) {
        LOGGER.debug(
                "SET writing the status of the persisted test case result with name=[%s] from [%s] to [%s]",
                this.testCase.getName(),
                this.status,
                status
        );

        this.status = status;
    }

    /**
     * Gets the metadata map associated to the result test case.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result ");

        return Collections.unmodifiableMap(this.metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
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
