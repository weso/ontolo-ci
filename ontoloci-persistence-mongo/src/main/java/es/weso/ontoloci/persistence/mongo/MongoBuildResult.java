package es.weso.ontoloci.persistence.mongo;

import es.weso.ontoloci.worker.build.BuildResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class MongoBuildResult implements Serializable {

    @Id
    private String buildId;
    private BuildResult buildResult;

    /**
     * Public empty constructor
     */
    public MongoBuildResult() {}

    /**
     * Public constructor for springboot.
     *
     * @param buildResult to set
     */
    public MongoBuildResult(String buildId, BuildResult buildResult) {
        this.buildId = buildId;
        this.buildResult = buildResult;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public BuildResult getBuildResult() {
        return buildResult;
    }

    public void setBuildResult(BuildResult buildResult) {
        this.buildResult = buildResult;
    }
}
