package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HubTests {

    // Repository data example
    private final String owner = "mistermboy";
    private final String repo = "oci-test";
    private final String branch = "main";
    private final int numberOfEntries = 3;


    @Test
    public void getTestCasesTest() throws IOException {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        Collection<HubTestCase> testCases = gitHubService.getTestCases(owner,repo,branch);
        assertFalse(testCases.isEmpty());
        assertEquals(testCases.size(),numberOfEntries);

    }


    @Test
    public void getPersonalTokenTest(){
        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        gitHubService.getPersonalAccessToken("");
    }
}
