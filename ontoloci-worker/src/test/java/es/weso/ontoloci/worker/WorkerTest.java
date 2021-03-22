package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.HubImplementation;
import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.MockedRepositoryProvider;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.build.BuildResultStatus;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import es.weso.shapeMaps.ShapeMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WorkerTest {

    private final static String DEFAULT_OWNER = "weso";
    private final static String DEFAULT_REPO = "ontolo-ci-test";
    private final static String DEFAULT_COMMIT = "DEFAULT_COMMIT";
    private final static String FAILURE_COMMIT = "FAILURE_COMMIT";
    private final static String EXCEPTION_COMMIT = "EXCEPTION_COMMIT";
    private final static String FILE_NOT_FOUND_COMMIT = "FILE_NOT_FOUND_COMMIT";
    private final static String EMPTY_FILE_COMMIT = "EMPTY_FILE_COMMIT";
    private final static String WRONG_FILE_CONTENT_COMMIT = "WRONG_FILE_CONTENT_COMMIT";

    private static Build defaultBuild;
    private static Build failureBuild;
    private static Build cancelledBuild;
    private static Build fileNotFoundBuild;
    private static Build emptyFileBuild;
    private static Build wrongContentFileBuild;


    @BeforeAll
    public static void setUp(){

        Map<String,String> metadata = new HashMap<>();
        metadata.put("owner",DEFAULT_OWNER);
        metadata.put("repo",DEFAULT_REPO);

        defaultBuild = Build.from(new ArrayList<>());
        metadata.put("commit",DEFAULT_COMMIT);
        defaultBuild.setMetadata(new HashMap<>(metadata));

        failureBuild = Build.from(new ArrayList<>());
        metadata.put("commit",FAILURE_COMMIT);
        failureBuild.setMetadata(new HashMap<>(metadata));

        cancelledBuild = Build.from(new ArrayList<>());
        metadata.put("commit",EXCEPTION_COMMIT);
        cancelledBuild.setMetadata(new HashMap<>(metadata));

        fileNotFoundBuild = Build.from(new ArrayList<>());
        metadata.put("commit",FILE_NOT_FOUND_COMMIT);
        fileNotFoundBuild.setMetadata(new HashMap<>(metadata));

        emptyFileBuild = Build.from(new ArrayList<>());
        metadata.put("commit",EMPTY_FILE_COMMIT);
        emptyFileBuild.setMetadata(new HashMap<>(metadata));

        wrongContentFileBuild = Build.from(new ArrayList<>());
        metadata.put("commit",WRONG_FILE_CONTENT_COMMIT);
        wrongContentFileBuild.setMetadata(new HashMap<>(metadata));


    }

    @Test
    public void workerExecutorTest(){

        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential, MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(defaultBuild);

        assertNotNull(buildResult);
        assertTrue(buildResult.getTestCaseResults().size()>0);

    }
    

    @Test
    public void workerSequentialTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());
        HubBuild hubBuild = defaultBuild.toHubBuild();
        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);
        defaultBuild = Build.from(hubBuild);

        WorkerSequential workerSequential = new WorkerSequential();
        BuildResult buildResult = workerSequential.executeBuild(defaultBuild);

        assertNotNull(buildResult);
        assertTrue(buildResult.getTestCaseResults().size()>0);

    }

    
    @Test
    public void metadataTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(defaultBuild);
        assertNotNull(buildResult.getMetadata());
    }

    @Test
    public void buildResultSuccessStatusTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(defaultBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.SUCCESS);
    }


    @Test
    public void testCaseResultSuccessStatusTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(defaultBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.SUCCESS);
        for(TestCaseResult testCaseResult:buildResult.getTestCaseResults()){
            assertEquals(testCaseResult.getStatus(), TestCaseResultStatus.SUCCESS);
        }
    }

    @Test
    public void buildResultFailureStatusTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(failureBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.FAILURE);
    }

    @Test
    public void testCaseResultFailureStatusTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(failureBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.FAILURE);
        for(TestCaseResult testCaseResult:buildResult.getTestCaseResults()){
            assertEquals(testCaseResult.getStatus(), TestCaseResultStatus.FAILURE);
        }
    }

    @Test
    public void buildResultCancelledStatusTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(cancelledBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.CANCELLED);
    }

    @Test
    public void buildResultFileNotFoundExceptionTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(fileNotFoundBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.CANCELLED);
        assertEquals(buildResult.getMetadata().get("exceptions"), "true");
        assertEquals(buildResult.getMetadata().get("checkTitle"), "FileNotFound");
    }

    @Test
    public void buildResultEmptyFileExceptionTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(emptyFileBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.CANCELLED);
        assertEquals(buildResult.getMetadata().get("exceptions"), "true");
        assertEquals(buildResult.getMetadata().get("checkTitle"), "EmptyContentFile");
    }

    @Test
    public void buildValidationErrorExceptionTest() {
        WorkerSequential workerSequential = new WorkerSequential();
        WorkerExecutor workerExecutor = WorkerExecutor.from(workerSequential,MockedRepositoryProvider.empty());
        BuildResult buildResult = workerExecutor.executeBuild(wrongContentFileBuild);
        assertEquals(buildResult.getStatus(), BuildResultStatus.CANCELLED);
        assertEquals(buildResult.getMetadata().get("exceptions"), "true");
        assertEquals(buildResult.getMetadata().get("checkTitle"), "ValidationError");
    }


    @Test
    public void validationTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());
        HubBuild hubBuild = defaultBuild.toHubBuild();
        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);
        defaultBuild = Build.from(hubBuild);

        TestCase testCase = defaultBuild.getTestCases().iterator().next();
        Validate v = new Validate();
        ShapeMap resultValidation = v.validateStr(
                testCase.getOntology(),
                testCase.getInstances(),
                testCase.getSchema(),
                testCase.getProducedShapeMap()).unsafeRunSync();

        assertNotNull(resultValidation);
        assertTrue(resultValidation.toJson().spaces2().length()>0);
    }


    @Test
    public void validationWithExpectedResultTest(){

        HubImplementation ontolociHubImplementation = new HubImplementation(MockedRepositoryProvider.empty());
        HubBuild hubBuild = defaultBuild.toHubBuild();
        hubBuild = ontolociHubImplementation.fillBuild(hubBuild);
        defaultBuild = Build.from(hubBuild);

        TestCase testCase = defaultBuild.getTestCases().iterator().next();
        Validate v = new Validate();
        ResultValidation resultValidation = new ResultValidation();

        assertNull(resultValidation.getResultShapeMap());
        assertNull(resultValidation.getExpectedShapeMap());

        resultValidation = v.validateStrResultValidation(
                testCase.getOntology(),
                testCase.getInstances(),
                testCase.getSchema(),
                testCase.getProducedShapeMap(),
                testCase.getExpectedShapeMap()).unsafeRunSync();

        assertNotNull(resultValidation.getResultShapeMap());
        assertNotNull(resultValidation.getExpectedShapeMap());

    }

}
