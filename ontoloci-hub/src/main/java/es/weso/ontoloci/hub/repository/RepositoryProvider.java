package es.weso.ontoloci.hub.repository;
import es.weso.ontoloci.hub.test.HubTestCase;

import java.io.IOException;
import java.util.Collection;

/**
 * This interface sets the contract for all the repository providers possible implementations
 * @author Pablo Menéndez
 */
public interface RepositoryProvider {

    /**
     * Gets a collection of test cases from a concrete commit of a repository provider.
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

    /**
     * Updates the checkrun status for a concrete checkRunId of a repo
     *
     * @param authToken             authorization token
     * @param checkRunId            id of the checkRun
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param conclusion            new status of the checkrun
     * @param output                message
     *
     */
    void updateCheckRun(
            final String authToken,
            final String checkRunId,
            final String owner,
            final String repo,
            final String conclusion,
            final String output);
}
