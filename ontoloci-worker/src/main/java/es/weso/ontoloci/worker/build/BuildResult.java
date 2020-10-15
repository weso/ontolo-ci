package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.worker.test.TestCaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BuildResult {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildResult.class);

    private String id;
    private Collection<TestCaseResult> testCaseResults;

    /**
     * Factory method that creates a new instance of build result from an array of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static BuildResult from(final TestCaseResult... testCaseResults) {
        return new BuildResult("", Arrays.asList(testCaseResults));
    }

    /**
     * Factory method that creates a new instance of build result from a collection of test case results.
     *
     * @param testCaseResults from which to create the build result instance.
     * @return a new instance of build result from an array of test case results.
     */
    public static BuildResult from(final Collection<TestCaseResult> testCaseResults) {
        return new BuildResult("", testCaseResults);
    }

    /**
     * Private constructor for build results. It takes a collection of the test results.
     *
     * @param testCaseResults from which to create the build result.
     */
    private BuildResult(final String id, final Collection<TestCaseResult> testCaseResults) {
        this.testCaseResults = testCaseResults;

        LOGGER.debug("Creating a new build result for " + testCaseResults);
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

    @Override
    public String toString() {
        return "BuildResult{" +
                "id='" + id + '\'' +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
