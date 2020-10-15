package es.weso.ontoloci.persistence.mongo;

import es.weso.ontoloci.worker.build.BuildResult;
import org.springframework.data.annotation.Id;

public class BuildResultMongoObject {

    @Id
    private String id;
    private BuildResult buildResult;

    public static BuildResultMongoObject from(BuildResult buildResult) {
        return new BuildResultMongoObject(buildResult);
    }

    public BuildResultMongoObject() {}

    private BuildResultMongoObject(BuildResult buildResult) {
        this.id = buildResult.getId();
        this.buildResult = buildResult;
    }

    public BuildResult asBuildResult() {
        BuildResult br = BuildResult.from(this.buildResult.getTestCaseResults());
        br.setId(this.id);
        return br;
    }

    public String getId() {
        return buildResult.getId();
    }

    public void setId(String id) {
        this.id = id;
        this.buildResult.setId(id);
    }

    public BuildResult getBuildResult() {
        return buildResult;
    }

    public void setBuildResult(BuildResult buildResult) {
        this.buildResult = buildResult;
    }
}
