package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.test.TestCase;

public class nosecomollamarte {





    public void hazCosas(Build build){


        String owner = "mistermboy";
        String repo = "oci-test";
        String branch = "main";
        String ontologyFolder = "src";
        String testFolder = "test";

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        for(TestCase t:gitHubService.getTestCases(owner,repo,branch,ontologyFolder,testFolder)) {
            System.out.println(t.toString());
        }

    }

}
