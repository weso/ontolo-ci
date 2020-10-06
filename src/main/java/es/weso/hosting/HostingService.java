package es.weso.hosting;

import es.weso.model.Manifest;

public interface HostingService {

	Manifest[] getValidationData(String owner,String repo, String branch,String ontologyFolder,String testFolder); 
	
}
