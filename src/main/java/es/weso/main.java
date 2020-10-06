package es.weso;

import es.weso.hosting.impl.GitHubService;
import es.weso.model.Manifest;

/**
 * 
 * Sorry about this...
 *
 */

public class main {

	public static void main(String[] args) {
	
		String owner = "mistermboy";
		String repo = "oci-test";
		String branch = "main";
		String ontologyFolder = "src";
		String testFolder = "test";
		
		GitHubService ghService = new GitHubService();
		for(Manifest m:ghService.getValidationData(owner,repo,branch,ontologyFolder,testFolder)) {
			System.out.println(m.toString());
		}
			
	}

}
