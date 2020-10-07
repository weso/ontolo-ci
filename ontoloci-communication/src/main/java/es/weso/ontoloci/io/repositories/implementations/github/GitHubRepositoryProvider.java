package es.weso.ontoloci.io.repositories.implementations.github;

import es.weso.ontoloci.io.repositories.Repository;
import es.weso.ontoloci.io.repositories.RepositoryProvider;
import es.weso.ontoloci.validation.manifest.Manifest;

public class GitHubRepositoryProvider implements RepositoryProvider {

    private static final String NAME = "GitHub";
    private static final String API_ADDRESS = "https://raw.githubusercontent.com/";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getAPIAddress() {
        return API_ADDRESS;
    }

    @Override
    public Manifest getManifest(Repository repository) {
        return null;
    }
}
