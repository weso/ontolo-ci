package es.weso.ontoloci.hub.repository;
import es.weso.ontoloci.worker.test.TestCase;

import java.util.Collection;

public interface RepositoryProviderService {

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
    Collection<TestCase> getTestCases(String owner, String repo, String branch, String ontologyFolder, String testFolder);
}
