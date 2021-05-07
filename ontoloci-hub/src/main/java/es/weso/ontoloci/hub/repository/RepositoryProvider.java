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
     * Gets a collection of test cases from a specific commit of a repository provider.
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param commit                sha of the commit
     *
     * @return test cases
     * @throws IOException
     */
    Collection<HubTestCase> getTestCases(
            final String owner,
            final String repo,
            final String commit
    ) throws Exception;


    /**
     * Creates a checkrun for a specific commit of a repository
     *
     * @param owner     of the repository
     * @param repo      name of the repository
     * @param commit    sha of the commit
     *
     * @return  id of the created checkrun
     * @throws IOException
     */
    String createCheckRun(
            final String owner,
            final String repo,
            final String commit) throws IOException;

    /**
     * Updates the checkrun status for a specific checkRunId of a repository
     *
     * @param checkRunId            id of the checkRun
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param conclusion            new status of the checkrun
     * @param output                message
     *
     * @return  info of the updated checkrun
     * @throws IOException
     */
    String updateCheckRun(
            final String checkRunId,
            final String owner,
            final String repo,
            final String conclusion,
            final String output) throws IOException;
}
