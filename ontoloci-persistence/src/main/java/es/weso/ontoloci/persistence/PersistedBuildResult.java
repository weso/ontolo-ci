package es.weso.ontoloci.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PersistedBuildResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistedBuildResult.class);

    private String id;
    private Map<String, String> metadata;
    private Collection<PersistedTestCaseResult> testCaseResults;

    /**
     * Factory method that creates a new instance of build result from an array of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static PersistedBuildResult from(final Map<String, String> metadata,final PersistedTestCaseResult... testCaseResults) {
        return new PersistedBuildResult("",metadata, Arrays.asList(testCaseResults));
    }

    /**
     * Factory method that creates a new instance of build result from an array of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static PersistedBuildResult from(final PersistedTestCaseResult... testCaseResults) {
        return new PersistedBuildResult("", Arrays.asList(testCaseResults));
    }

    /**
     * Factory method that creates a new instance of build result from a collection of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static PersistedBuildResult from(final Map<String, String> metadata,final Collection<PersistedTestCaseResult> testCaseResults) {
        return new PersistedBuildResult("",metadata, testCaseResults);
    }

    /**
     * Private constructor for build results. It takes a collection of the test results.
     *
     * @param metadata from which to create the build result.
     * @param testCaseResults from which to create the build result.
     */
    public PersistedBuildResult(final String id, final Map<String, String> metadata,final Collection<PersistedTestCaseResult> testCaseResults) {
        this.metadata = metadata;
        this.testCaseResults = testCaseResults;

        LOGGER.debug("Creating a new build result for ");
    }

    /**
     * Private constructor for build results. It takes a collection of the test results.
     *
     * @param testCaseResults from which to create the build result.
     */
    public PersistedBuildResult(final String id, final Collection<PersistedTestCaseResult> testCaseResults) {
        this.testCaseResults = testCaseResults;

        LOGGER.debug("Creating a new build result for ");
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Collection<PersistedTestCaseResult> getTestCaseResults() {
        return Collections.unmodifiableCollection(this.testCaseResults);
    }

    public void addTestCaseResults(List<PersistedTestCaseResult> testCaseResults) {
        System.out.println(testCaseResults.getClass());
        testCaseResults.forEach(item -> this.testCaseResults.add(item));
    }

    /**
     * Gets the metadata map associated to the persisted build result.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result " + this.metadata.toString()
                + " from " + this);

        return Collections.unmodifiableMap(this.metadata);
    }

    @Override
    public String toString() {
        return "BuildResult{" +
                "id='" + id + '\'' +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
