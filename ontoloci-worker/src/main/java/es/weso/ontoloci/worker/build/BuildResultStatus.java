package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.persistence.PersistedTestCaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the values that the build result status may take:
 *
 *  - PASS when the test finishes and the result is positive.
 *  - FAIL when the test finishes and the result is not positive.
 *
 * @author Pablo Menéndez
 */
public enum BuildResultStatus {

    /**
     * Represents a build that has been executed and the result is positive.
     */
    PASS("pass"),

    /**
     * Represents a build that has been executed and the result is not positive.
     */
    FAIL("fail");

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
    BuildResultStatus(String value) {
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
        return "BuildResultStatus{" +
                "value='" + value + '\'' +
                '}';
    }
}
