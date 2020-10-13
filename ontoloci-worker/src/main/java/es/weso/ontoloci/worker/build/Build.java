package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.worker.test.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Build {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(Build.class);

    private Collection<TestCase> testCases;

    /**
     * Factory method that creates a build instance from an array of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static Build from(final TestCase... testCases) {
        LOGGER.debug("Factory method creating a new build for " + testCases);
        return new Build(Arrays.asList(testCases));
    }

    /**
     * Factory method that creates a build instance from a list of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static Build from(final Collection<TestCase> testCases) {
        LOGGER.debug("Factory method creating a new build for " + testCases);
        return new Build(testCases);
    }

    /**
     * Private constructor. It creates a build instance from a collection of test cases.
     *
     * @param testCases from which to create the build.
     */
    private Build(final Collection<TestCase> testCases) {
        this.testCases = testCases;

        LOGGER.debug("Creating a new build for " + this);
    }

    /**
     * Gets an unmodifiable collection of the collection of test cases.
     *
     * @return an unmodifiable collection of the collection of test cases.
     */
    public Collection<TestCase> getTestCases() {
        return Collections.unmodifiableCollection(this.testCases);
    }

    /**
     * Sets the tests cases collection to the given one.
     *
     * @param testCases collection to be set.
     */
    public void setTestCases(final Collection<TestCase> testCases) {
        this.testCases = testCases;
    }
}
