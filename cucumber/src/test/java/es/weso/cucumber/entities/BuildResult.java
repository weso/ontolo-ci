package es.weso.cucumber.entities;

import es.weso.ontoloci.worker.build.BuildResultStatus;
import java.util.*;

public class BuildResult {

    private String id;
    private Map<String, String> metadata;
    private BuildResultStatus status;
    private Collection<TestCaseResult> testCaseResults;

    public BuildResult(){}


    public void setId(final String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }

    public Collection<TestCaseResult> getTestCaseResults() {
        return Collections.unmodifiableCollection(this.testCaseResults);
    }


    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(this.metadata);
    }

    public BuildResultStatus getStatus() {
        return status;
    }
    public void setStatus(BuildResultStatus status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "BuildResult{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", status=" + status +
                ", testCaseResults=" + testCaseResults +
                '}';
    }
}
