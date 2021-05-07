package es.weso.ontoloci.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PersistedBuildResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistedBuildResult.class);

    private String id;
    private Map<String, String> metadata;
    private PersistedBuildResultStatus status;
    private Collection<PersistedTestCaseResult> testCaseResults;


    /**
     * Factory method that creates a new instance of build result from an id,
     * a Map of metadata, a status and an array of test case results.
     *
     * @param id
     * @param metadata
     * @param status
     * @param testCaseResults
     * @return a new instance of persisted build result
     */
    public static PersistedBuildResult from(final String id,final Map<String, String> metadata,final PersistedBuildResultStatus status, final Collection<PersistedTestCaseResult> testCaseResults) {
        return new PersistedBuildResult(id, metadata,status,testCaseResults);
    }

    /**
     * Factory method that creates a new instance of persisted build result from an array of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static PersistedBuildResult from(final String id,final Map<String, String> metadata, final PersistedTestCaseResult... testCaseResults) {
        return new PersistedBuildResult(id, metadata, Arrays.asList(testCaseResults));
    }

    /**
     * Factory method that creates a new instance of persisted build result from an array of test case results.
     *
     * @param testCaseResults from which to create the persisted build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static PersistedBuildResult from(final PersistedTestCaseResult... testCaseResults) {
        return new PersistedBuildResult(UUID.randomUUID().toString(), Arrays.asList(testCaseResults));
    }

    /**
     * Factory method that creates a new instance of persisted build result from a collection of test case results.
     *
     * @param testCaseResults from which to create the persisted build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static PersistedBuildResult from(final String id,final Map<String, String> metadata, final Collection<PersistedTestCaseResult> testCaseResults) {
        return new PersistedBuildResult(id, metadata, testCaseResults);
    }

    /**
     * Private constructor for build results.
     *
     * @param metadata        from which to create the persisted build result.
     * @param testCaseResults from which to create the persisted build result.
     */
    private PersistedBuildResult(final String id, final Map<String, String> metadata, final Collection<PersistedTestCaseResult> testCaseResults) {
        this.id = id;
        this.metadata = metadata;
        this.testCaseResults = testCaseResults;

        LOGGER.debug("Creating a new build result for ");
    }

    /***
     * Private constructor for build results.
     * @param id
     * @param metadata
     * @param status
     * @param testCaseResults
     */
    private PersistedBuildResult(final String id, final Map<String, String> metadata,final PersistedBuildResultStatus status,final Collection<PersistedTestCaseResult> testCaseResults) {
        this.id = id;
        this.metadata = metadata;
        this.status = status;
        this.testCaseResults = testCaseResults;

        LOGGER.debug("Creating a new build result for ");
    }

    /**
     * Private constructor for build results
     *
     * @param id              from which to create the persisted build result.
     * @param testCaseResults from which to create the persisted build result.
     */
    private PersistedBuildResult(final String id, final Collection<PersistedTestCaseResult> testCaseResults) {
        this.id = id;
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
        LOGGER.debug("Getting the metadata of the test case result "
                + " from " + this);

        return Collections.unmodifiableMap(this.metadata);
    }

    public PersistedBuildResultStatus getStatus() {
        return status;
    }

    public void setStatus(PersistedBuildResultStatus status) {
        this.status = status;
    }

    public boolean isEmpty(){
        return this.metadata != null? false:true;
    }

    @Override
    public String toString() {
        return "PersistedBuildResult{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", status=" + status +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
