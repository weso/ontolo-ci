package es.weso.ontoloci.hub.repository.impl;

import es.weso.ontoloci.hub.exceptions.EmptyContentFileException;
import es.weso.ontoloci.hub.repository.RepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MockedRepositoryProvider implements RepositoryProvider {

    public static MockedRepositoryProvider empty() {
        return new MockedRepositoryProvider();
    }

    @Override
    public Collection<HubTestCase> getTestCases(String owner, String repo, String commit) throws Exception {
        switch (commit) {
            case FAILURE_COMMIT:
                return getFailureTestCases();
            case EXCEPTION_COMMIT:
                return getExceptionTestCases();
            case FILE_NOT_FOUND_COMMIT:
                return getFileNotFoundTestCases();
            case EMPTY_FILE_COMMIT:
                return getEmptyFileTestCases();
            case WRONG_FILE_CONTENT_COMMIT:
                return getWrongFileContentTestCases();
            default:
                return getSuccessFullTestCases();

        }
    }

    @Override
    public String createCheckRun(String owner, String repo, String commit) throws IOException {
        return "created";
    }

    @Override
    public String updateCheckRun(String checkRunId, String owner, String repo, String conclusion, String output) throws IOException {
        return "updated";
    }


    private Collection<HubTestCase> getExceptionTestCases() throws Exception {
        throw new Exception();
    }

    private Collection<HubTestCase> getFileNotFoundTestCases() throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    private Collection<HubTestCase> getEmptyFileTestCases(){
        throw new EmptyContentFileException();
    }


    private Collection<HubTestCase> getWrongFileContentTestCases(){
        Collection<HubTestCase> testCases = new ArrayList<>();
        for(int i=1;i<=3;i++){
            testCases.add(new HubTestCase("Mocked Test "+i,"fail",instances,getSchema(i),getSMIn(i),getSMOut(i)));
        }
        return testCases;
    }

    private Collection<HubTestCase> getSuccessFullTestCases(){
        Collection<HubTestCase> testCases = new ArrayList<>();
        for(int i=1;i<=3;i++){
            testCases.add(new HubTestCase("Mocked Test "+i,ontology,instances,getSchema(i),getSMIn(i),getSMOut(i)));
        }
        return testCases;
    }

    private Collection<HubTestCase> getFailureTestCases(){
        Collection<HubTestCase> testCases = new ArrayList<>();
        testCases.add(new HubTestCase("Mocked Failure Test",ontology,instances,schema1,shapeMapIn1,failedShapeMapOut1));
        return testCases;
    }


    private String getSchema(int i){
        switch (i) {
            case 1:
                return schema1;
            case 2:
                return schema2;
            default:
                return schema3;

        }
    }

    private String getSMIn(int i) {
        switch (i) {
            case 1:
                return shapeMapIn1;
            case 2:
                return shapeMapIn2;
            default:
                return shapeMapIn3;

        }
    }

    private String getSMOut(int i) {
        switch (i) {
            case 1:
                return shapeMapOut1;
            case 2:
                return shapeMapOut2;
            default:
                return shapeMapOut3;

        }
    }


    private final static String EXCEPTION_COMMIT = "EXCEPTION_COMMIT";
    private final static String FILE_NOT_FOUND_COMMIT = "FILE_NOT_FOUND_COMMIT";
    private final static String EMPTY_FILE_COMMIT = "EMPTY_FILE_COMMIT";
    private final static String FAILURE_COMMIT = "FAILURE_COMMIT";
    private final static String WRONG_FILE_CONTENT_COMMIT = "WRONG_FILE_CONTENT_COMMIT";


    private final static String ontology = "@prefix weso: <http://www.weso.com/> . @prefix owl: <http://www.w3.org/2002/07/owl#> . weso:Investigador a owl:Class . weso:Doctor a owl:Class . weso:Estudiante a owl:Class . ";
    private final static String instances = "@prefix weso: <http://www.weso.com/> . @prefix owl: <http://www.w3.org/2002/07/owl#> . weso:Labra a weso:Investigador . weso:Oscar a weso:Doctor . weso:Pablo a weso:Estudiante . ";

    private final static String schema1 = "prefix weso: <http://www.weso.com/> prefix owl: <http://www.w3.org/2002/07/owl#> weso:ShapeTest1{ a [weso:Investigador] }";
    private final static String schema2 = "prefix weso: <http://www.weso.com/> prefix owl: <http://www.w3.org/2002/07/owl#> weso:ShapeTest2{ a [weso:Doctor] } ";
    private final static String schema3 = "prefix weso: <http://www.weso.com/> prefix owl: <http://www.w3.org/2002/07/owl#> weso:ShapeTest3{ a [weso:Estudiante] } ";

    private final static String shapeMapIn1 = "weso:Labra@weso:ShapeTest1";
    private final static String shapeMapIn2 = "weso:Oscar@weso:ShapeTest2";
    private final static String shapeMapIn3 = "weso:Pablo@weso:ShapeTest3";

    private final static String shapeMapOut1 = "weso:Labra@weso:ShapeTest1";
    private final static String shapeMapOut2 = "weso:Oscar@weso:ShapeTest2";
    private final static String shapeMapOut3 = "weso:Pablo@weso:ShapeTest3";

    private final static String failedShapeMapOut1 = "weso:Labra@!weso:ShapeTest1";

}
