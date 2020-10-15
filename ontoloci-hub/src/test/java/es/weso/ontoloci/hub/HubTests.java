package es.weso.ontoloci.hub;


import es.weso.ontoloci.hub.manifest.Manifest;
import es.weso.ontoloci.hub.manifest.ManifestEntry;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.worker.test.TestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HubTests {

    // Repository data example
    private final String owner = "mistermboy";
    private final String repo = "oci-test";
    private final String branch = "main";
    private final String ontologyFolder = "src";
    private final String testFolder = "test";
    private final int numberOfEntries = 3;


    @Test
    public void getTestCasesTest() {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        Collection<TestCase> testCases = gitHubService.getTestCases(owner,repo,branch,ontologyFolder,testFolder);
        assertFalse(testCases.isEmpty());
        assertEquals(testCases.size(),numberOfEntries);

    }
}