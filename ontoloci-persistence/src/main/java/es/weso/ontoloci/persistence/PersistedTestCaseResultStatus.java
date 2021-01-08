package es.weso.ontoloci.persistence;

/**
 * Represents the values that the test result status may take.
 * <p>
 * A test case can be:<p>
 *  - WAITING to be executed.<p>
 *  - EXECUTING when the test is under execution.<p>
 *  - PASS when the test finishes and the result is positive.<p>
 *  - FAIL when the test finishes and the result is not positive.<p>
 *
 * @author Guillermo Facundo Colunga
 * @version 20201007
 */
public enum PersistedTestCaseResultStatus {

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

    /**
     * Stores the string value of the enum.
     */
    private final String value;

    /**
     * Main constructor for enums.
     *
     * @param value corresponds to the string value for the enum.
     */
    PersistedTestCaseResultStatus(String value) {
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
}
