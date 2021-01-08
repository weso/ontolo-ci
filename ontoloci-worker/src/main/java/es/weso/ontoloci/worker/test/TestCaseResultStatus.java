package es.weso.ontoloci.worker.test;

import es.weso.ontoloci.persistence.PersistedTestCaseResult;
import es.weso.ontoloci.persistence.PersistedTestCaseResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the values that the test result status may take.
 * <p>
 * A test case can be:<p>
 *  - WAITING to be executed.<p>
 *  - EXECUTING when the test is under execution.<p>
 *  - SUCCESS when the test finishes and the result is positive.<p>
 *  - FAIL when the test finishes and the result is not positive.<p>
 *
 * @author Guillermo Facundo Colunga
 * @version 20201007
 */
public enum TestCaseResultStatus {

    /**
     * Represents that the test case has not been executed yet.
     */
    WAITING("waiting"),

    /**
     * Represents that the test case is being executed.
     */
    EXECUTING("executing"),

    /**
     * Represents a test case that has been executed and the result is positive.
     */
    SUCCESS("success"),

    /**
     * Represents a test case that has been executed and the result is not positive.
     */
    FAILURE("failure");

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistedTestCaseResult.class);

    /**
     * Stores the string value of the enum.
     */
    private final String value;

    /**
     * Main constructor for enums.
     *
     * @param value corresponds to the string value for the enum.
     */
    TestCaseResultStatus(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of the enum. This string value is intended to be use in toString methods.
     *
     * @return an string contining the string representation of the enumerator.
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "TestCaseResultStatus{" +
                    "value='" + value + '\'' +
                '}';
    }

    public static PersistedTestCaseResultStatus toPersistedTestCaseResultStatus(TestCaseResultStatus status) {
        LOGGER.debug(
                String.format("NEW creating a new PersistedTestCaseResultStatus from the static factory for TestCaseResultStatus with status=[%s]",
                    status.getValue()
                )
        );
        if(status.equals(EXECUTING))
            return PersistedTestCaseResultStatus.EXECUTING;
        else if(status.equals(WAITING))
            return PersistedTestCaseResultStatus.WAITING;
        else if(status.equals(FAILURE))
            return PersistedTestCaseResultStatus.FAILURE;
        else if(status.equals(SUCCESS))
            return PersistedTestCaseResultStatus.SUCCESS;
        else
            return PersistedTestCaseResultStatus.FAILURE;
    }

    public static TestCaseResultStatus fromPersistedTestCaseResultStatus(PersistedTestCaseResultStatus persistedStatus) {
        if(persistedStatus.equals(PersistedTestCaseResultStatus.EXECUTING))
            return EXECUTING;
        else if(persistedStatus.equals(PersistedTestCaseResultStatus.WAITING))
            return WAITING;
        else if(persistedStatus.equals(PersistedTestCaseResultStatus.FAILURE))
            return FAILURE;
        else if(persistedStatus.equals(PersistedTestCaseResultStatus.SUCCESS))
            return SUCCESS;
        else
            return FAILURE;
    }
}
