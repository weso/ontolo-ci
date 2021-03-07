package es.weso.ontoloci.worker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.build.BuildResultStatus;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.PrefixedNode;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.ShapeMapResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import es.weso.rdf.Prefix;
import es.weso.rdf.PrefixMap;
import es.weso.rdf.nodes.IRI;
import es.weso.shapeMaps.ShapeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple3;
import scala.util.Either;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * The sequential worker
 * @author Pablo Menéndez
 */
public class WorkerSequential implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerSequential.class);

    private long initBuildTime;
    private long stopBuildTime;
    private long initTestTime;
    private long stopTestTime;

    /**
     * Validates all the tests from a build and count the time it takes.
     * Fills the build with metadata.
     * Finally sets the build status result.
     *
     * @param build to allocate in the worker.
     * @return buildRestult with all the validated tests and the metadata
     */
    @Override
    public BuildResult executeBuild(Build build) {

        // 1. Init counting execution time of the build
        startBuildCrono();
        // 2. Validate tests
        final Collection<TestCaseResult> testCaseResults = validateTests(build.getTestCases());
        // 3. Stop counting execution time of the build.
        stopBuildCrono();
        // 4. Resolve build result status
        BuildResultStatus buildResultStatus = resolveBuildStatus(testCaseResults);
        // 5. Get the metadata
        final Map<String, String> metadata = fillBuildMetadata(build,buildResultStatus);
        // 6. Create the build result
        BuildResult buildResult = BuildResult.from(build.getId(),metadata,testCaseResults);
        // 7. Set the build result status
        buildResult.setStatus(buildResultStatus);
        // 8. Finally return the Build result.
        return buildResult;

    }


    /**
     * Validates all the tests from a collection an count the time each test takes.
     * Fills each test with metadata.
     * Finally sets each test status result.
     *
     * @param testCases to be validated
     * @return test case results
     */
    private Collection<TestCaseResult> validateTests(Collection<TestCase> testCases){
        final Collection<TestCaseResult> testCaseResults = new ArrayList<>();
        TestCaseResult currentTestCase = null;
        for(TestCase testCase : testCases) {
            // 1. Create the result object.
            currentTestCase = TestCaseResult.from(testCase);
            // 2. Set the status to executing
            currentTestCase.setStatus(TestCaseResultStatus.EXECUTING);
            // 3. Init counting execution time.
            startTestCrono();
            // 4. Validate the test case
            ResultValidation resultValidation = validateTest(testCase);
            // 5. Compare results
            compareResults(resultValidation,currentTestCase);
            // 6. Stop counting execution time.
            stopTestCrono();
            // 7. Add the metadata
            fillTestMetadata(currentTestCase);
            // And finally add it to the collection of results.
            testCaseResults.add(currentTestCase);
        }
        return testCaseResults;
    }


    /**
     * Checks each test result from a collection and determines if the build has passed without errors or not.
     * If any of the test results has failed, then the build has failed too.
     *
     * @param testCaseResults test results
     * @return build result status
     */
    private BuildResultStatus resolveBuildStatus(Collection<TestCaseResult> testCaseResults) {
        for(TestCaseResult testCase : testCaseResults) {
            if(testCase.getStatus() == TestCaseResultStatus.FAILURE)
                return BuildResultStatus.FAILURE;
        }
        return BuildResultStatus.SUCCESS;
    }

    /**
     * Compares the produced validation results with the expected validation results.
     * If any produced shapeMap status has not the same expected status then the test has failed.
     * Then produced and expected results are stored in the metadata of the TestCaseResult:
     *
     *  - Produced: Represents the produced ShapeMap where the nodes and the shapes are prefixed
     *  - Expected: Represents the expected ShapeMap where the nodes and the shapes are prefixed
     *  - Produced_output: Represents the full produced ShapeMap
     *  - Expected_output: Represents the full expected ShapeMap
     *
     *
     * @param resultValidation  result of the validation
     * @param testCaseResult    test case result
     */
    private void compareResults(ResultValidation resultValidation,TestCaseResult testCaseResult){
        List<ShapeMapResultValidation> expected = getExpectedResult(resultValidation);
        List<ShapeMapResultValidation> produced = getProducedResult(resultValidation);
        TestCaseResultStatus status = TestCaseResultStatus.SUCCESS;

        for(ShapeMapResultValidation e: expected){
            for(ShapeMapResultValidation p: produced){
                if(checkNodes(e,p))
                    if(!checkStatus(e,p))
                        status = TestCaseResultStatus.FAILURE;
            }
        }
        testCaseResult.setStatus(status);
        testCaseResult.addMetadata("produced",toJson(produced));
        testCaseResult.addMetadata("expected",toJson(expected));
        testCaseResult.addMetadata("produced_output",resultValidation.getResultShapeMap().toJson().spaces2());
        testCaseResult.addMetadata("expected_output",resultValidation.getExpectedShapeMap().toJson().spaces2());
    }




    /**
     * Checks if the passed nodes of a ShapeMap result are the same
     *
     * @param e the first shapeMap result
     * @param p the first shapeMap result
     * @return  if the nodes are the same
     */
    private boolean checkNodes(ShapeMapResultValidation e,ShapeMapResultValidation p){
        String expectedNode = e.getNode();
        String producedNode = p.getNode();
        return  expectedNode.equals(producedNode);
    }

    /**
     * Checks if the status of the passed ShapeMap result are the same
     *
     * @param e the first shapeMap result
     * @param p the first shapeMap result
     * @return  if the statuses are the same
     */
    private boolean checkStatus(ShapeMapResultValidation e,ShapeMapResultValidation p){
        String expectedStatus = e.getStatus();
        String producedStatus = p.getStatus();
        return  expectedStatus.equals(producedStatus);
    }


    /**
     * Performs the validation of a specific test case.
     *
     * @param testCase to be validated
     * @return the result of the validation
     */
    private ResultValidation validateTest(TestCase testCase){
        Validate v = new Validate();
        ResultValidation resultValidation = v.validateStrResultValidation(
                testCase.getOntology(),
                testCase.getInstances(),
                testCase.getSchema(),
                testCase.getProducedShapeMap(),
                testCase.getExpectedShapeMap()).unsafeRunSync();
        return resultValidation;
    }

    /**
     * Gets the expected result from a ResultValidation object as a list of shapeMaps result validation.
     * Sets the prefixes for each ShapeMapResultValidation.
     *
     * @param resultValidation result of validation
     * @return expected result
     */
    private List<ShapeMapResultValidation> getExpectedResult(ResultValidation resultValidation){
        final List<ShapeMapResultValidation> expected =  getResultFromValidation(resultValidation.getExpectedShapeMap());
        // Now add the prefixes
        for(ShapeMapResultValidation e: expected){
            PrefixedNode nodePrefix = getPrefix(resultValidation.getResultShapeMap().nodesPrefixMap(),e.getNode());
            PrefixedNode shapePrefix = getPrefix(resultValidation.getExpectedShapeMap().shapesPrefixMap(),e.getShape());
            e.setNodePrefix(nodePrefix);
            e.setShapePrefix(shapePrefix);
        }
        return expected;
    }



    /**
     * Gets the produced result from a ResultValidation object as a list of shapeMaps result validation.
     * The shex validator usually infers more things that we need, so we are only keeping shape map results
     * that are in the expected result too.
     * It also sets the prefixes for each ShapeMapResultValidation.
     *
     * @param resultValidation result of validation
     * @return produced result
     */
    private List<ShapeMapResultValidation> getProducedResult(ResultValidation resultValidation){
        final List<ShapeMapResultValidation> expected = getResultFromValidation(resultValidation.getExpectedShapeMap());
        final List<ShapeMapResultValidation> produced = getResultFromValidation(resultValidation.getResultShapeMap());
        final List<ShapeMapResultValidation> cleanProduced = new ArrayList<>();

        for(ShapeMapResultValidation e: expected){
            for(ShapeMapResultValidation p: produced){
                if(e.getNode().equals(p.getNode())){
                    String node = p.getNode();
                    String shape = p.getShape();
                    String status = p.getStatus();
                    String info = p.getAppInfo();
                    String reason = p.getReason();
                    PrefixedNode nodePrefix = getPrefix(resultValidation.getResultShapeMap().nodesPrefixMap(),node);
                    PrefixedNode shapePrefix = getPrefix(resultValidation.getExpectedShapeMap().shapesPrefixMap(),shape);

                    cleanProduced.add(new ShapeMapResultValidation(node,shape,status,info,reason,nodePrefix,shapePrefix));
                }
            }
        }
        return cleanProduced;
    }

    /***
     * Given an iri as a string, looks for the appropriate prefix in a PrefixMap
     *
     * @param prefixMap where to look for
     * @param iriStr    iri as a string
     * @return  prefix as a PrefixNode obj
     */
    private PrefixedNode getPrefix(PrefixMap prefixMap, String iriStr) {
        PrefixedNode prefix = new PrefixedNode();

        try {
            String subIri = iriStr.substring(1, iriStr.length() - 1 );
            IRI iri = new IRI(new URI(subIri));
            Either<String, Tuple3<Prefix, IRI, String>> prefixLocalName = prefixMap.getPrefixLocalName(iri);
            Tuple3<Prefix, IRI, String> tuple =  prefixLocalName.toOption().get();
            prefix = new PrefixedNode(tuple._1().str(),tuple._2().str(),tuple._3());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
         return prefix;
    }

    /**
     * Maps a shape map into a list of ShapeMapResultValidation objects
     *
     * @param shapeMap shapeMap
     * @return list of ShapeMapResultValidation objects
     */
    private List<ShapeMapResultValidation> getResultFromValidation(ShapeMap shapeMap){
        ObjectMapper jsonMapper  = new ObjectMapper(new JsonFactory());
        try {
            return Arrays.asList(jsonMapper.readValue(shapeMap.toJson().spaces2(), ShapeMapResultValidation[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Replaces the current test metadata with a new map of metadata that contains the test execution time.
     * @param testCase test case
     */
    private void fillTestMetadata(TestCaseResult testCase){
        final Map<String, String> metadata = new HashMap<>(testCase.getMetadata());
        metadata.put("execution_time", getTestTime());
        testCase.setMetadata(metadata);
    }

    /**
     * Creates a new metadata Map with:
     *
     *      checkTitle      ->  Title for the check run
     *      checkBody       ->  Body msg for the check run
     *      execution_time  ->  Execution time of the build
     *      execution_date  ->  Execution date of the build
     *
     * @param build             build
     * @param buildStatus       status of the build
     * @return  build metadata
     */
    private Map<String, String> fillBuildMetadata(Build build,BuildResultStatus buildStatus){
        final Map<String, String> buildMetadata = new HashMap<>(build.getMetadata());

        String checkTitle = "Build Passing";
        if(buildStatus == BuildResultStatus.FAILURE)
          checkTitle = "Build Failing";

        buildMetadata.put("checkTitle",checkTitle);
        buildMetadata.put("execution_time",getBuildTime());
        buildMetadata.put("execution_date", String.valueOf(System.currentTimeMillis()));
        return buildMetadata;
    }

    /**
     * Given a list of result shape maps validation, returns a processable json for the front
     *
     * @param results   list of result shape maps validation
     * @return results as a json
     */
    private String toJson(List<ShapeMapResultValidation> results) {
        String json="[";
        for(ShapeMapResultValidation s:results){
            json+=s.toJson()+",";
        }
        json=json.substring(0,json.length()-1)+"]";
        return json.replace("\n", "").replace("\r", "");
    }


    /******  CRONO *******/


    /**
     * Initializes  build crono
     */
    private void startBuildCrono(){
        initBuildTime = System.nanoTime();
    }

    /**
     * Stops build crono
     */
    private void stopBuildCrono(){
        stopBuildTime = System.nanoTime();
    }

    /**
     * Initializes test crono
     */
    private void startTestCrono(){
        initTestTime = System.nanoTime();
    }

    /**
     * Stops test crono
     */
    private void stopTestCrono(){
        stopTestTime = System.nanoTime();
    }


    /**
     * Gets the build execution time in seconds.
     * @return execution time
     */
    private String getBuildTime(){
        return getExecTimeFormated(initBuildTime,stopBuildTime);
    }

    /**
     * Gets the test execution time in seconds.
     * @return execution time
     */
    private String getTestTime(){
        return getExecTimeFormated(initTestTime,stopTestTime);
    }

    /**
     * Calculate the elapsed time between two times in seconds.
     *
     * @param initTime initial time
     * @param stopTime final time
     * @return elapsed time in seconds
     */
    private String getExecTimeFormated(long initTime,long stopTime){
        final long executionTimeNS = stopTime - initTime;
        final double seconds = (double)executionTimeNS/ 1_000_000_000;
        return String.format("%f sec",seconds);
    }


}
