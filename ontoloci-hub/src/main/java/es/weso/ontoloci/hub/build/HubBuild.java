package es.weso.ontoloci.hub.build;

import es.weso.ontoloci.hub.test.HubTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HubBuild {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(HubBuild.class);

    private Map<String, String> metadata;
    private Collection<HubTestCase> testCases;

    /**
     * Factory method that creates a build instance from an array of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static HubBuild from(final HubTestCase... testCases) {
        LOGGER.debug("Factory method creating a new build for " + testCases);
        return new HubBuild(Arrays.asList(testCases),new HashMap<>());
    }

    /**
     * Factory method that creates a build instance from a list of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static HubBuild from(final Collection<HubTestCase> testCases) {
        LOGGER.debug("Factory method creating a new build for " + testCases);
        return new HubBuild(testCases,new HashMap<>());
    }

    /**
     * Private constructor. It creates a build instance from a collection of test cases.
     *
     * @param testCases from which to create the build.
     */
    private HubBuild(final Collection<HubTestCase> testCases, Map<String, String> metadata) {
        this.testCases = testCases;
        this.metadata = metadata;

        LOGGER.debug("Creating a new build for " + this);
    }

    /**
     * Gets an unmodifiable collection of the collection of test cases.
     *
     * @return an unmodifiable collection of the collection of test cases.
     */
    public Collection<HubTestCase> getTestCases() {
        return Collections.unmodifiableCollection(this.testCases);
    }

    /**
     * Sets the tests cases collection to the given one.
     *
     * @param testCases collection to be set.
     */
    public void setTestCases(final Collection<HubTestCase> testCases) {
        this.testCases = testCases;
    }

    /**
     * Gets the metadata map associated to the build.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result " + this.metadata.toString()
                + " from " + this);

        return Collections.unmodifiableMap(this.metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }


}
