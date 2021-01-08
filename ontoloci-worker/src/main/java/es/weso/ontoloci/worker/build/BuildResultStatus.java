package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.persistence.PersistedBuildResultStatus;
import es.weso.ontoloci.persistence.PersistedTestCaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the values that the build result status may take:
 *
 *  - SUCCESS   when the build finishes and the result is positive.
 *  - FAILURE   when the build finishes and the result is not positive.
 *  - CANCELLED when te build is not executed.
 *
 * @author Pablo Menéndez
 */
public enum BuildResultStatus {


    /**
     * Represents a build that has been executed and the result is positive.
     */
    SUCCESS("success"),

    /**
     * Represents a build that has been executed and the result is not positive.
     */
    FAILURE("failure"),

    /**
     * Represents a build that hasn't been executed
     */
    CANCELLED("cancelled");


    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistedTestCaseResult.class);

    /**
     * Stores the string value of the enum.
     */
    private final String value;

    /**
     * Main constructor for enums.
     *
     * @param value corresponds into the string value for the enum.
     */
    BuildResultStatus(String value) {
        this.value = value;
    }

    /**
     * Transforms a BuildResultStatus into a PersistedBuildResultStatus
     * @param status build result status
     * @return persisted build result status
     */
    public static PersistedBuildResultStatus toPersistedBuildResultStatus(BuildResultStatus status) {
        if(status==BuildResultStatus.SUCCESS)
            return  PersistedBuildResultStatus.SUCCESS;
        if(status==BuildResultStatus.FAILURE)
            return  PersistedBuildResultStatus.FAILURE;

        return  PersistedBuildResultStatus.CANCELLED;
    }

    /**
     * Transforms a PersistedBuildResultStatus to a BuildResultStatus
     *
     * @param status persisted build result status
     * @return build result status
     */
    public static BuildResultStatus from(PersistedBuildResultStatus status) {
        if(status==PersistedBuildResultStatus.SUCCESS)
            return  BuildResultStatus.SUCCESS;
        if(status==PersistedBuildResultStatus.FAILURE)
            return  BuildResultStatus.FAILURE;

        return  BuildResultStatus.CANCELLED;
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
