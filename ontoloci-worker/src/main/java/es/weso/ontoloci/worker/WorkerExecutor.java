package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.weso.ontoloci.hub.OntolociHubImplementation;

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

        // Store the result of the build.
        final BuildResult buildResult = this.worker.executeBuild(build);

        persistence.save(BuildResult.toPersistedBuildResult(buildResult));

        return buildResult;
    }
}
