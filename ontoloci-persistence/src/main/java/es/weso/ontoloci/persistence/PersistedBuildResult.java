package es.weso.ontoloci.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PersistedBuildResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistedBuildResult.class);

    private String id;
    private Collection<PersistedTestCaseResult> testCaseResults;

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
    public static PersistedBuildResult from(final Collection<PersistedTestCaseResult> testCaseResults) {
        return new PersistedBuildResult("", testCaseResults);
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

    @Override
    public String toString() {
        return "BuildResult{" +
                "id='" + id + '\'' +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
