package es.weso.ontoloci.io.repositories;

import es.weso.ontoloci.validation.manifest.Manifest;

public interface RepositoryProviderOperations {

    /**
     * Gets the manifest from the repository provider.
     *
     * @return the manifest file.
     */
    Manifest getManifest(Repository repository);
}
