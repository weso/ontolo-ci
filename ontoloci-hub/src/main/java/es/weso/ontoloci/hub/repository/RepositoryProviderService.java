package es.weso.ontoloci.hub.repository;

import es.weso.ontoloci.hub.manifest.Manifest;

import java.util.Collection;

public interface RepositoryProviderService {

    Collection<Manifest> getValidationData(String owner,String repo, String branch,String ontologyFolder,String testFolder);
}
