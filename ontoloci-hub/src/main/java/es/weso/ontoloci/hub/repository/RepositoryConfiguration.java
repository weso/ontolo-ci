package es.weso.ontoloci.hub.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The repository configuration models the YAML file that each repo that implements ontolo-ci needs to own.
 * At the moment it only contains the path to the manifest file where the test cases are defined,
 * the path to the ontology folder where the ontology is defined and the path to the test folder
 * where the tests are defined.
 *
 * @author Pablo Menénded Suárez
 */
public class RepositoryConfiguration {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryConfiguration.class);

    private String manifestPath;
    private String ontologyFolder;
    private String testFolder;

    /**
     * RepositoryConfiguration public constructor
     *
     * @param manifestPath path of the manifest file inside the repository.
     * @param ontologyFolder path of the ontology folder inside the repository.
     * @param testFolder path of the tests folder inside the repository.
     */
    public RepositoryConfiguration(final String manifestPath, final String ontologyFolder, final String testFolder) {
        this.manifestPath = manifestPath;
        this.ontologyFolder = ontologyFolder;
        this.testFolder = testFolder;

        LOGGER.debug(
                "Creating a new RepositoryConfiguration from the public constructor with manifest path=[%s], " +
                        "ontology folder=[%s] and tests folder=[%s]",
                manifestPath,
                ontologyFolder,
                testFolder
        );
    }

    /**
     * This empty constructor is needed for the YAML mapper.
     */
    public RepositoryConfiguration() {}


    /**
     * Gets the path of the manifest file.
     * @return the manifest file path.
     */
    public String getManifestPath() {
        return manifestPath;
    }

    /**
     * Gets the path of the ontology folder.
     * @return the ontology folder path.
     */
    public String getOntologyFolder() {
        return ontologyFolder;
    }

    /**
     * Gets the path of the test folder.
     * @return the test folder path.
     */
    public String getTestFolder() {
        return testFolder;
    }

    /**
     * Sets the path of the manifest file.
     * @param manifestPath to be set.
     */
    public void setManifestPath(String manifestPath) {
        this.manifestPath = manifestPath;
    }

    /**
     * Sets the path of the ontology folder
     * @param ontologyFolder to be set.
     */
    public void setOntologyFolder(String ontologyFolder) {
        this.ontologyFolder = ontologyFolder;
    }

    /**
     * Sets the path of the test folder
     * @param testFolder to be set.
     */
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
