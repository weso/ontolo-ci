package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.persistence.PersistedBuildResult;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BuildResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildResult.class);

    private String id;
    private Map<String, String> metadata;
    private BuildResultStatus status;
    private Collection<TestCaseResult> testCaseResults;


    /**
     * Factory method that creates a new instance of build result from an array of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static BuildResult from(final String id,final Map<String, String> metadata,final BuildResultStatus status,final Collection<TestCaseResult> testCaseResults) {
        return new BuildResult(id,metadata, status,testCaseResults);
    }

    /**
     * Factory method that creates a new instance of build result from a collection of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static BuildResult from(final String id,final Map<String, String> metadata,final Collection<TestCaseResult> testCaseResults) {
        return new BuildResult(id, metadata, testCaseResults);
    }

    /**
     * Private constructor for build results.
     * @param id
     * @param metadata
     * @param testCaseResults
     */
    private BuildResult(final String id,final Map<String, String> metadata, final Collection<TestCaseResult> testCaseResults) {
        this.id = id;
        this.testCaseResults = testCaseResults;
        this.metadata = metadata;
        this.status = BuildResultStatus.FAILURE; // I know it´s weird that the status value it´s fail by default but it simplifies the things
        LOGGER.debug("Creating a new build result for ");
    }

    /***
     * Private constructor for build results.
     * @param id
     * @param metadata
     * @param status
     * @param testCaseResults
     */
    private BuildResult(final String id,final Map<String, String> metadata,final BuildResultStatus status,final Collection<TestCaseResult> testCaseResults) {
        this.id = id;
        this.testCaseResults = testCaseResults;
        this.metadata = metadata;
        this.status = status;
        LOGGER.debug("Creating a new build result for ");
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Collection<TestCaseResult> getTestCaseResults() {
        return Collections.unmodifiableCollection(this.testCaseResults);
    }

    public void addTestCaseResults(List<TestCaseResult> testCaseResults) {
        System.out.println(testCaseResults.getClass());
        testCaseResults.forEach(item -> this.testCaseResults.add(item));
    }

    /**
     * Gets the metadata map associated to the build result.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result " + this.metadata.toString()
                + " from " + this);

        return Collections.unmodifiableMap(this.metadata);
    }

    public BuildResultStatus getStatus() {
        return status;
    }

    public void setStatus(BuildResultStatus status) {
        this.status = status;
    }

    /**
     * Transforms a PersistedBuildResult into a BuildResult
     *
     * @param persistedBuildResult
     * @return build result
     */
    public static BuildResult from(PersistedBuildResult persistedBuildResult) {
        return new BuildResult(
                persistedBuildResult.getId(),
                persistedBuildResult.getMetadata(),
                BuildResultStatus.from(persistedBuildResult.getStatus()),
                persistedBuildResult.getTestCaseResults().stream()
                        .map(item -> TestCaseResult.from(item))
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    /**
     * Transforms a BuildResult into a PersistedBuildResult
     *
     * @param buildResult
     * @return persisted build result
     */
    public static PersistedBuildResult toPersistedBuildResult(BuildResult buildResult) {
        return PersistedBuildResult.from(
                buildResult.getId(),
                buildResult.getMetadata(),
                BuildResultStatus.toPersistedBuildResultStatus(buildResult.getStatus()),
                buildResult.getTestCaseResults().stream()
                        .map(item -> TestCaseResult.toPersistedTestCaseResult(item))
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }


    @Override
    public String toString() {
        return "BuildResult{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", status=" + status +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
