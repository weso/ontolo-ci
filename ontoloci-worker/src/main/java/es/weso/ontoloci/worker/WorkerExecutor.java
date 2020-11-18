package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.utils.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.weso.ontoloci.hub.OntolociHubImplementation;

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
     * Main constructor for the worker exeutor class. This is intended for dependency injection.
     *
     * @param worker to execute the builds.
     */
    private WorkerExecutor(final Worker worker) {
        this.worker = worker;
    }

    @Override
    public BuildResult executeBuild(Build build) {
        LOGGER.debug("Executing a nre build for " + build);

        OntolociHubImplementation ontolocyHub = new OntolociHubImplementation();
        //Transform the current build to a HubBuild
        HubBuild hubBuild = build.toHubBuild();
        //Add the tests
        hubBuild = ontolocyHub.addTestsToBuild(hubBuild);

        //Transform the returned HubBuild to a Build and overwrites the result
        build = build.from(hubBuild);

        BuildResult buildResult = BuildResult.from(build.getMetadata(), new ArrayList<TestCaseResult>());
        String title = build.getMetadata().get("checkTitle");
        String body = build.getMetadata().get("checkBody");
        String conclusion = "failure";
        if(!build.getMetadata().get("exceptions").equals("true")){
            // Store the result of the build.
            buildResult = this.worker.executeBuild(build);
            conclusion = buildResult.getMetadata().get("buildResult").equals("PASS") ? "success" : "failure";
            title = buildResult.getMetadata().get("checkTitle");
            body = MarkdownUtils.getMarkDownFromTests(buildResult.getTestCaseResults());
        }

        String output = "{\"title\":\""+title+"\",\"summary\":\""+body+"\"}";
        ontolocyHub.updateCheckRun(conclusion,output);

        for(TestCaseResult tcr : buildResult.getTestCaseResults()) {
            System.out.println(tcr.getTestCase().getName() + " -> " + tcr.getStatus());
        }

        LOGGER.debug("INTERNAL validation finished, storing results in persistence layer");
        persistence.save(BuildResult.toPersistedBuildResult(buildResult));

        return buildResult;
    }
}
