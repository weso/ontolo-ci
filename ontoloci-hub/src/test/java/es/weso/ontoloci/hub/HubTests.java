package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HubTests {

    // Repository for testing (https://github.com/weso/ontolo-ci-test)
    private final static String DEFAULT_OWNER = "weso";
    private final static String DEFAULT_REPO = "ontolo-ci-test";
    private final static String DEFAULT_COMMIT = "1ad23547eca78153327b4b0c005a43f0907964c1";
    private final static String EXCEPTION_COMMIT = "b01db1082105ea0600bbf983bbff775aa563263b";
    private final static String FILE_NOT_FOUND_COMMIT = "dd34aac295450521ec0698bd8c0a768897f7915c";
    private final static String EMPTY_FILE_COMMIT = "1e874d755408e40840c90f043acc941dc705e398";


    @Test
    public void addTestsToBuildTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation();

        HubBuild hubBuild = HubBuild.from();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner", DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);
        metadata.put("commit", DEFAULT_COMMIT);
        hubBuild.setMetadata(metadata);

        assertTrue(hubBuild.getTestCases().size()<=0);
        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);
        assertTrue(hubBuild.getTestCases().size()>0);

    }


    @Test
    public void addTestsToBuildWithExceptionsTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation();

        HubBuild hubBuild = HubBuild.from();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner", DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);
        metadata.put("commit", EXCEPTION_COMMIT);
        hubBuild.setMetadata(metadata);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNull(hubBuild.getMetadata().get("exceptions"));

        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNotNull(hubBuild.getMetadata().get("exceptions"));
        assertEquals(hubBuild.getMetadata().get("exceptions"),"true");

    }

    @Test
    public void addTestsToBuildWithoutExceptionsTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation();

        HubBuild hubBuild = HubBuild.from();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner", DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);
        metadata.put("commit", DEFAULT_COMMIT);
        hubBuild.setMetadata(metadata);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNull(hubBuild.getMetadata().get("exceptions"));

        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);

        assertTrue(hubBuild.getTestCases().size()>0);
        assertNotNull(hubBuild.getMetadata().get("exceptions"));
        assertEquals(hubBuild.getMetadata().get("exceptions"),"false");

    }

    @Test
    public void fileNotFoundTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation();

        HubBuild hubBuild = HubBuild.from();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner", DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);
        metadata.put("commit", FILE_NOT_FOUND_COMMIT);
        hubBuild.setMetadata(metadata);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNull(hubBuild.getMetadata().get("exceptions"));
        assertNull(hubBuild.getMetadata().get("checkTitle"));

        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNotNull(hubBuild.getMetadata().get("exceptions"));
        assertNotNull(hubBuild.getMetadata().get("checkTitle"));
        assertEquals(hubBuild.getMetadata().get("exceptions"),"true");
        assertEquals(hubBuild.getMetadata().get("checkTitle"),"FileNotFound");

    }

    @Test
    public void emptyContentFileTest() throws IOException {

        HubImplementation ontolociHubImplementation = new HubImplementation();

        HubBuild hubBuild = HubBuild.from();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner", DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);
        metadata.put("commit", EMPTY_FILE_COMMIT);
        hubBuild.setMetadata(metadata);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNull(hubBuild.getMetadata().get("exceptions"));
        assertNull(hubBuild.getMetadata().get("checkTitle"));

        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);

        assertTrue(hubBuild.getTestCases().size()<=0);
        assertNotNull(hubBuild.getMetadata().get("exceptions"));
        assertNotNull(hubBuild.getMetadata().get("checkTitle"));
        assertEquals(hubBuild.getMetadata().get("exceptions"),"true");
        assertEquals(hubBuild.getMetadata().get("checkTitle"),"EmptyContentFile");

    }



    @Test
    public void getTestCasesFromGHRepoTest() throws IOException {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        Collection<HubTestCase> testCases = gitHubService.getTestCases(DEFAULT_OWNER,DEFAULT_REPO, DEFAULT_COMMIT);
        assertNotNull(testCases);
        assertFalse(testCases.isEmpty());
        assertEquals(testCases.size(),2);

    }

    @Test
    public void createGHCheckRunTest() throws IOException {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        String checkRunId = gitHubService.createCheckRun(DEFAULT_OWNER,DEFAULT_REPO,DEFAULT_COMMIT);
        assertTrue(checkRunId.length()>=1);
        assertNotNull(checkRunId);

    }


    @Test
    public void updateGHCheckRunTest() throws IOException {

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        String checkRunId = gitHubService.createCheckRun(DEFAULT_OWNER,DEFAULT_REPO,DEFAULT_COMMIT);

        assertTrue(checkRunId.length()>=1);
        assertNotNull(checkRunId);

        String result = gitHubService.updateCheckRun(checkRunId, DEFAULT_OWNER,DEFAULT_REPO,"success","{\"title\":\"Tests Passed\",\"summary\":\"All tests have pass\"}");

        assertTrue(result.length()>=1);
        assertNotNull(result);

    }


}
