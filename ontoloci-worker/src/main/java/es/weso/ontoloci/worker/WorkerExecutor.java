package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.build.BuildResultStatus;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.utils.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.weso.ontoloci.hub.OntolociHubImplementation;

import javax.swing.plaf.ButtonUI;
import java.util.*;

public class WorkerExecutor implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildResult.class);

    private final OntolociDAO persistence = OntolociInMemoryDAO.instance();

    private final Worker worker;

    public static WorkerExecutor from(Worker worker) {
        LOGGER.debug("Static factory creating a new worker executor for " + worker);
        return new WorkerExecutor(worker);
    }
    
    /**
     * Main constructor for the worker executor class. This is intended for dependency injection.
     * @param worker to execute the builds.
     */
    private WorkerExecutor(final Worker worker) {
        this.worker = worker;
    }

    @Override
    public BuildResult executeBuild(Build build) {
        LOGGER.debug("Executing build: " + build);
        // 1. Create a Hub instance
        OntolociHubImplementation ontolocyHub = new OntolociHubImplementation();
        // 2. Transform the current build to a HubBuild
        HubBuild hubBuild = build.toHubBuild();
        // 3. Add the tests to the build
        hubBuild = ontolocyHub.addTestsToBuild(hubBuild);
        // 4. Transform the HubBuild to a worker build
        build = Build.from(hubBuild);
        // 5. Execute worker in case everything went right
        BuildResult buildResult = executeWorker(build);
        // 6. Update the check run
        updateCheckRun(ontolocyHub,buildResult);
        // 7. Persist the build result
        persist(buildResult);
        // 8. Finally return the build result
        return buildResult;
    }

    private void updateCheckRun(OntolociHubImplementation ontolocyHub, BuildResult buildResult) {
        String conclusion = getConclusion(buildResult);
        String output = getOutput(buildResult);
        ontolocyHub.updateCheckRun(conclusion,output);
    }


    private BuildResult executeWorker(Build build){
      return !hasExceptions(build) ?
              this.worker.executeBuild(build) :
              BuildResult.from(build.getMetadata(), new ArrayList<>());
    }

    private void persist(BuildResult buildResult) {
        LOGGER.debug("INTERNAL validation finished, storing results in persistence layer");
        persistence.save(BuildResult.toPersistedBuildResult(buildResult));
    }


    private String getOutput(BuildResult buildResult) {
        String title = getTilte(buildResult);
        String body = getBody(buildResult);
        return "{\"title\":\""+title+"\",\"summary\":\""+body+"\"}";
    }

    private String getTilte(BuildResult buildResult) {
        return buildResult.getMetadata().get("checkTitle");
    }

    private String getBody(BuildResult buildResult) {
        return !hasExceptions(buildResult) ?
                MarkdownUtils.getMarkDownFromTests(buildResult.getTestCaseResults()) :
                buildResult.getMetadata().get("checkBody");
    }

    private String getConclusion(BuildResult buildResult) {
       return buildResult.getStatus().getValue();
    }

    private boolean hasExceptions(Build build){
        return build.getMetadata().get("exceptions").equals("true");
    }

    private boolean hasExceptions(BuildResult buildResult){
        return buildResult.getMetadata().get("exceptions").equals("true");
    }

}
