package es.weso.ontoloci.hub;


import es.weso.ontoloci.hub.manifest.Manifest;
import es.weso.ontoloci.hub.manifest.ManifestEntry;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.worker.test.TestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HubTests {

    // Repository data example
    private final String owner = "mistermboy";
    private final String repo = "oci-test";
    private final String branch = "main";
    private final String ontologyFolder = "src";
    private final String testFolder = "test";


    @Test
    public void getTestCasesTest() {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        for(TestCase t:gitHubService.getTestCases(owner,repo,branch,ontologyFolder,testFolder)) {

            assertEquals("",t.toString());
        }
    }
}
