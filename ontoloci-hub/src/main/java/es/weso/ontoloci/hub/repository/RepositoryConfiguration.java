package es.weso.ontoloci.hub.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The repository configuration models the YAML file that each repo that implements ontolo-ci needs to have.
 * At the moment it only contains the path to the manifest file where the test cases are defined,
 * the path to the ontology folder where the ontology is defined and the path to the test folder
 * where the tests are defined
 *
 * @author Pablo Menénded Suárez
 */
public class RepositoryConfiguration {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryConfiguration.class);

    // Stores the path of the manifest file inside the repository.
    private String manifestPath;
    // Stores the path of the ontology folder inside the repository.
    private String ontologyFolder;
    // Stores the path of the tests folder inside the repository.
    private String testFolder;

    public RepositoryConfiguration() {} // Needed for the YAML mapper

    public RepositoryConfiguration(final String manifestPath, final String ontologyFolder, final String testsFolder) {
        this.manifestPath = manifestPath;
        this.ontologyFolder = ontologyFolder;
        this.testFolder = testsFolder;

        LOGGER.debug(
                "Creating a new RepositoryConfiguration from the public constructor with manifest path=[%s], " +
                        "ontology folder=[%s] and tests folder=[%s]",
                manifestPath,
                ontologyFolder,
                testsFolder
        );
    }

    public String getManifestPath() {
        return manifestPath;
    }

    public void setManifestPath(String manifestPath) {
        this.manifestPath = manifestPath;
    }

    public String getOntologyFolder() {
        return ontologyFolder;
    }

    public String getTestFolder() {
        return testFolder;
    }

    public void setOntologyFolder(String ontologyFolder) {
        this.ontologyFolder = ontologyFolder;
    }

    public void setTestFolder(String testFolder) {
        this.testFolder = testFolder;
    }

    @Override
    public String toString() {
        return "RepositoryConfiguration{" +
                "manifestPath='" + manifestPath + '\'' +
                ", ontologyFolder='" + ontologyFolder + '\'' +
                ", testFolder='" + testFolder + '\'' +
                '}';
    }
}
