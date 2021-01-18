package es.weso.ontoloci.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the values that the build result status may take:
 *
 *  - SUCCESS when the build finishes and the result is positive.
 *  - FAILURE when the build finishes and the result is not positive.
 *  - CANCELLED when te build is not executed.
 *
 * @author Pablo Menéndez
 */

public enum PersistedBuildResultStatus {

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
         * @param value corresponds to the string value for the enum.
         */
        PersistedBuildResultStatus(String value) {
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
            return "PersistedBuildResultStatus{" +
                    "value='" + value + '\'' +
                    '}';
        }
}
