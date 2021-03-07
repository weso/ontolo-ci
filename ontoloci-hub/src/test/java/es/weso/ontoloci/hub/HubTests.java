package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.repository.impl.MockedRepositoryProvider;
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
    private final static String DEFAULT_COMMIT = "DEFAULT_COMMIT";
    private final static String EXCEPTION_COMMIT = "EXCEPTION_COMMIT";
    private final static String FILE_NOT_FOUND_COMMIT = "FILE_NOT_FOUND_COMMIT";
    private final static String EMPTY_FILE_COMMIT = "EMPTY_FILE_COMMIT";


    @Test
    public void fillBuildTest(){
        HubImplementation hub = new HubImplementation(MockedRepositoryProvider.empty());

        HubBuild hubBuild = HubBuild.from();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner", DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);
        metadata.put("commit", DEFAULT_COMMIT);
        hubBuild.setMetadata(metadata);

        assertTrue(hubBuild.getTestCases().size()<=0);
        hubBuild = hub.fillBuild(hubBuild);
        assertTrue(hubBuild.getTestCases().size()>0);
    }


    @Test
    public void fillBuildWithExceptionsTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());

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

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());

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

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());

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

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());

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
    public void createCheckRunTest() throws IOException {

        MockedRepositoryProvider mockedRepositoryProvider = MockedRepositoryProvider.empty();
        String checkRunId = mockedRepositoryProvider.createCheckRun(DEFAULT_OWNER,DEFAULT_REPO,DEFAULT_COMMIT);
        assertTrue(checkRunId.length()>=1);
        assertNotNull(checkRunId);

    }


    @Test
    public void updateCheckRunTest() throws IOException {

        MockedRepositoryProvider mockedRepositoryProvider = MockedRepositoryProvider.empty();
        String checkRunId = mockedRepositoryProvider.createCheckRun(DEFAULT_OWNER,DEFAULT_REPO,DEFAULT_COMMIT);

        assertTrue(checkRunId.length()>=1);
        assertNotNull(checkRunId);

        String result = mockedRepositoryProvider.updateCheckRun(checkRunId, DEFAULT_OWNER,DEFAULT_REPO,"","");

        assertTrue(result.length()>=1);
        assertNotNull(result);

    }

}
