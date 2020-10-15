package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.weso.ontoloci.hub.OntolociHubImplementation;

public class WorkerExecutor implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildResult.class);

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
        HubBuild hubBuild = build.toHubBuild();
        //build = ontolocyHub.addTestsToBuild(build);
        return this.worker.executeBuild(build);
    }
}
