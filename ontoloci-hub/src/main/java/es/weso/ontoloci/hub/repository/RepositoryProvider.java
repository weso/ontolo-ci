package es.weso.ontoloci.hub.repository;
import es.weso.ontoloci.worker.test.TestCase;

import java.util.Collection;

/**
 * TO-DO
 */
public interface RepositoryProvider {

    /**
     * Gets a collection of test cases from a concrete branch and commit of a repository service repository.
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param branch                of the repository
     * @param ontologyFolder        repository folder that contains the ontology
     * @param testFolder            repository folder that contains the tests
     *
     * @return test cases
     */
    Collection<TestCase> getTestCases(
            final String owner,
            final String repo,
            final String branch,
            final String ontologyFolder,
            final String testFolder
    );
}
