package es.weso.ontoloci.hub.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The repository configuration models the YAML file that each repo that implements ontolo-ci needs to have.
 * At the moment it only contains the path to the manifest file where the test cases are defined.
 *
 * @author Pablo Menénded Suárez
 */
public class RepositoryConfiguration {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryConfiguration.class);

    // Stores the path of the manifest file inside the repository.
    private String manifestPath;

    public RepositoryConfiguration() {} // Needed for the YAML mapper

    public RepositoryConfiguration(String manifestPath) {
        this.manifestPath = manifestPath;

        LOGGER.debug("Creating a new repository configuration for " + this);
    }

    public String getManifestPath() {
        return manifestPath;
    }

    public void setManifestPath(String manifestPath) {
        this.manifestPath = manifestPath;
    }

    @Override
    public String toString() {
        return "RepositoryConfiguration [manifestPath=" + manifestPath + "]";
    }
}
