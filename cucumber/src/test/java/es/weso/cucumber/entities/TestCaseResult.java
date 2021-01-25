package es.weso.cucumber.entities;

import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ShapeMapResultValidation;
import java.util.*;

public class TestCaseResult {

    private TestCase testCase;
    private TestCaseResultStatus status;
    private Map<String, String> metadata;
    private ShapeMapResultValidation resultValidation;



    public TestCaseResult() {
    }

    public TestCase getTestCase() { return this.testCase;  }

    public TestCaseResultStatus getStatus() { return status; }

    public void setStatus(TestCaseResultStatus status) {this.status = status; }

    public Map<String, String> getMetadata() { return Collections.unmodifiableMap(this.metadata); }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(String key,String value){
        this.metadata.put(key,value);
    }

    public ShapeMapResultValidation getResultValidation() { return resultValidation; }

    public void setResultValidation(ShapeMapResultValidation resultValidation) { this.resultValidation = resultValidation; }

    @Override
    public String toString() {
        return "TestCaseResult{" +
                "testCase=" + testCase +
                ", status=" + status +
                ", metadata=" + metadata +
                ", resultValidation=" + resultValidation +
                '}';
    }
}
