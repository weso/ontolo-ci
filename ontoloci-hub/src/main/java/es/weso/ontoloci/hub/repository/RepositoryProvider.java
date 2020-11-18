package es.weso.ontoloci.hub.repository;
import es.weso.ontoloci.hub.test.HubTestCase;

import java.io.IOException;
import java.util.Collection;

/**
 * TO-DO
 */
public interface RepositoryProvider {

    /**
     * Gets a collection of test cases from a concrete commit of a repository service repository.
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param commit                of the repository
     *
     * @return test cases
     */
    Collection<HubTestCase> getTestCases(
            final String owner,
            final String repo,
            final String commit
    ) throws IOException;


    void updateCheckRun(String authToken, String checkRunId, String owner, String repo, String conclusion, String output);
}
