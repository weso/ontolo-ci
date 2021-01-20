package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class HubTests {

    // Repository data example
    private final String owner = "mistermboy";
    private final String repo = "oci-test";
    private final String branch = "main";
    private final String commit = "701cf2151a14c6a41a145b0dc1e710d842cc4f56";
    private final int numberOfEntries = 3;


    @Test
    public void getTestCasesFromGHRepoTest() throws IOException {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        Collection<HubTestCase> testCases = gitHubService.getTestCases(owner,repo,branch);
        assertNotNull(testCases);
        assertFalse(testCases.isEmpty());
        assertEquals(testCases.size(),numberOfEntries);

    }

    @Test
    public void createGHCheckRunTest() throws IOException {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        String checkRunId = gitHubService.createCheckRun(owner,repo,branch);
        assertNotNull(checkRunId);

    }



    // CREAR TEST NEGATIVOS

}
