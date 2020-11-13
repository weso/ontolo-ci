package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.persistence.PersistedBuildResult;
import es.weso.ontoloci.worker.test.TestCaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BuildResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildResult.class);

    private String id;
    private Map<String, String> metadata;
    private Collection<TestCaseResult> testCaseResults;

    public static BuildResult from(PersistedBuildResult persistedBuildResult) {
        return new BuildResult(
                persistedBuildResult.getId(),
                persistedBuildResult.getMetadata(),
                persistedBuildResult.getTestCaseResults().stream()
                        .map(item -> TestCaseResult.from(item))
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    public static PersistedBuildResult toPersistedBuildResult(BuildResult buildResult) {
        return new PersistedBuildResult(
                buildResult.getId(),
                buildResult.getMetadata(),
                buildResult.getTestCaseResults().stream()
                        .map(item -> TestCaseResult.toPersistedTestCaseResult(item))
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    /**
     * Factory method that creates a new instance of build result from an array of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static BuildResult from(final Map<String, String> metadata,final TestCaseResult... testCaseResults) {
        return new BuildResult("",metadata, Arrays.asList(testCaseResults));
    }

    /**
     * Factory method that creates a new instance of build result from a collection of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static BuildResult from(final Map<String, String> metadata,final Collection<TestCaseResult> testCaseResults) {
        return new BuildResult("", metadata, testCaseResults);
    }

    /**
     * Private constructor for build results. It takes a map of metadata and a collection of the test results.
     *
     * @param metadata from which to create the build result.
     * @param testCaseResults from which to create the build result.
     */
    private BuildResult(final String id,final Map<String, String> metadata, final Collection<TestCaseResult> testCaseResults) {
        this.testCaseResults = testCaseResults;
        this.metadata = metadata;
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

    @Override
    public String toString() {
        return "BuildResult{" +
                "id='" + id + '\'' +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
