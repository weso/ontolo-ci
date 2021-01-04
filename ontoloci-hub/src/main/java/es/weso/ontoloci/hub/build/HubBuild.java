package es.weso.ontoloci.hub.build;

import es.weso.ontoloci.hub.test.HubTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Representation of a build for the Hub subsystem.
 * A HubBuild contains a collection of Hub test cases and a metadata map
 * @author Pablo Menéndez
 */
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
        LOGGER.debug(String.format("NEW Creating new HubBuild from the static factory with [%s] hub test cases And an empty HashMap<>()", testCases.length));
        return new HubBuild(Arrays.asList(testCases), new HashMap<>());
    }

    /**
     * Factory method that creates a build instance from a list of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static HubBuild from(final Collection<HubTestCase> testCases) {
        LOGGER.debug(String.format("NEW Creating new HubBuild from the static factory with [%s] hub test cases and an empty HashMap<>()", testCases.size()));
        return new HubBuild(testCases, new HashMap<>());
    }

    /**
     * Factory method that creates a build instance from a list of test cases
     * and a Map of metada
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static HubBuild from(final Collection<HubTestCase> testCases, final Map<String,String> metadata) {
        LOGGER.debug(String.format("NEW Creating new HubBuild from the static factory with [%s] hub test cases and [%s] metadata", testCases.size(), metadata));
        return new HubBuild(testCases, metadata);
    }

    /**
     * Private constructor. It creates a build instance from a collection of test cases.
     *
     * @param testCases from which to create the build.
     */
    private HubBuild(final Collection<HubTestCase> testCases, final Map<String, String> metadata) {
        this.testCases = testCases;
        this.metadata = metadata;

        LOGGER.debug(String.format("NEW Creating new HubBuild from the public constructor with [%s] hub test cases " + "and [%s] metadata", testCases.size(), metadata));
    }

    /**
     * Gets an unmodifiable collection of the collection of test cases.
     *
     * @return an unmodifiable collection of the collection of test cases.
     */
    public Collection<HubTestCase> getTestCases() {
        LOGGER.debug(String.format("GET reading the test cases of the HubTestCase, returning [%s] elements", this.testCases.size()));
        return Collections.unmodifiableCollection(this.testCases);
    }

    /**
     * Sets the tests cases collection to the given one.
     *
     * @param testCases collection to be set.
     */
    public void setTestCases(final Collection<HubTestCase> testCases) {
        LOGGER.debug(String.format("SET writing the test cases of the HubTestCase, new Collection has size of [%s]", this.testCases.size()));
        this.testCases = testCases;
    }

    /**
     * Gets the metadata map associated to the build.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug(String.format("GET reading the metadata of the test case result, returning [%s]", metadata.entrySet().size()));
        return Collections.unmodifiableMap(this.metadata);
    }


    /**
     * Sets the metadata to the given one.
     *
     * @param metadata to be set.
     */
    public void setMetadata(Map<String, String> metadata) {
        LOGGER.debug(String.format("SET writing the metadata of the HubTestCase, new Map has size of [%s]",this.metadata.entrySet().size()));
        this.metadata = metadata;
    }

}
