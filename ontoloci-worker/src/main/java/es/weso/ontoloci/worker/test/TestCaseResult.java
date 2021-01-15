package es.weso.ontoloci.worker.test;

import es.weso.ontoloci.persistence.PersistedTestCase;
import es.weso.ontoloci.persistence.PersistedTestCaseResult;
import es.weso.ontoloci.persistence.PersistedTestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ShapeMapResultValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCaseResult.class);

    // Final fields
    private final TestCase testCase;

    // Non final fields.
    private TestCaseResultStatus status;
    private Map<String, String> metadata;
    private ShapeMapResultValidation resultValidation;


    public static PersistedTestCaseResult toPersistedTestCaseResult(TestCaseResult testCaseResult) {

        LOGGER.debug(String.format("INTERNAL invoking the translation from TestCase to PersistedTestCase"));
        final PersistedTestCase persistedTestCase = TestCase.toPersistedTestCase(testCaseResult.getTestCase());

        LOGGER.debug(String.format("INTERNAL invoking the translation from TestCaseResult to PersistedTestCaseResult"));
        final PersistedTestCaseResult ptestCaseR = PersistedTestCaseResult.from(persistedTestCase);
        ptestCaseR.setMetadata(testCaseResult.getMetadata());

        LOGGER.debug(String.format("INTERNAL invoking the change of state of the PersistedTestCaseResult with name=[%s] from [%s] to [%s]", ptestCaseR.getTestCase().getName(), ptestCaseR.getStatus(), testCaseResult.getStatus()));
        final PersistedTestCaseResultStatus persistedStatus = TestCaseResultStatus.toPersistedTestCaseResultStatus(testCaseResult.getStatus());

        LOGGER.debug(String.format("INTERNAL created PersistedTestCaseResultStatus with status=[%s] form a TestCaseResultStatus with status=[%s]", ptestCaseR.getStatus(), testCaseResult.getStatus()));
        ptestCaseR.setStatus(persistedStatus);

        return  ptestCaseR;
    }

    public static TestCaseResult from(PersistedTestCaseResult persistedTestCaseResult) {

        LOGGER.debug(String.format("NEW creating a new TestCaseResult from a static factory and from a" + " PersistedTestCaseResult"));

        return new TestCaseResult(
                TestCase.from(persistedTestCaseResult.getTestCase()),
                TestCaseResultStatus.fromPersistedTestCaseResultStatus(persistedTestCaseResult.getStatus()),
                persistedTestCaseResult.getMetadata()
        );
    }

    /**
     * Factory method to create new instances of the test case result from the test case.
     * It initializes the test case result to the status waiting and empty metadata map.
     *
     * @param testCase from which to create the test case result.
     * @return the new instance of the rest case result.
     */
    public static TestCaseResult from(TestCase testCase) {
        LOGGER.debug("Static factory for creating a new test case result");
        return new TestCaseResult(testCase, TestCaseResultStatus.WAITING, new HashMap<>());
    }



    /**
     * Private constructor for test case results where need to set the test case,
     * the initial status and the metadata map.
     *
     * @param testCase that has been executed.
     * @param status of the test case execution.
     * @param metadata is a map with the test execution metadata.
     */
    private TestCaseResult(TestCase testCase, TestCaseResultStatus status, Map<String, String> metadata) {
        this.testCase = testCase;
        this.status = status;
        this.metadata = metadata;

        LOGGER.debug("Creating new test case result for");
    }




    /**
     * Gets the test case linked with this result test case.
     *
     * @return the test case linked with this result test case.
     */
    public TestCase getTestCase() {
        LOGGER.debug("Getting the value of the test case");

        return this.testCase;
    }

    /**
     * Gets the status of the execution of the linked tests case.
     *
     * @return the status of the execution of the linked tests case.
     */
    public TestCaseResultStatus getStatus() {
        LOGGER.debug("Getting the value of the test case result status");

        return status;
    }

    /**
     * Sets the status of the result test case.
     *
     * @param status to set.
     */
    public void setStatus(TestCaseResultStatus status) {
        LOGGER.debug("Setting the value of the test case result status from " + this.status + " to " + status);

        this.status = status;
    }

    /**
     * Gets the metadata map associated to the result test case.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result");

        return Collections.unmodifiableMap(this.metadata);
    }


    /**
     * Sets the metadata
     * @param metadata
     */
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    /**
     * Adds a new field to the metadata map
     *
     * @param key   of the new field
     * @param value of the new field
     */
    public void addMetadata(String key,String value){
        this.metadata.put(key,value);
    }

    /**
     * Gets the result validation as a ShapeMapResultValidation
     * @return result of the validation
     */
    public ShapeMapResultValidation getResultValidation() { return resultValidation; }

    /**
     * Sets the resultValidation
     * @param resultValidation
     */
    public void setResultValidation(ShapeMapResultValidation resultValidation) { this.resultValidation = resultValidation; }

    @Override
    public String toString() {
        return "TestCaseResult{" +
                "testCase=" + testCase +
                ", status=" + status +
                ", metadata=" + metadata +
                ", resultValidation=" + resultValidation +
                '}';
    }
}
