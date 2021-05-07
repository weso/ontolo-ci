package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.RepositoryProvider;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.PersistedBuildResult;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.build.BuildResultStatus;
import es.weso.ontoloci.worker.utils.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.weso.ontoloci.hub.HubImplementation;

import java.util.*;

public class WorkerExecutor implements Worker {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildResult.class);

    private final Worker worker;
    private final RepositoryProvider repositoryProvider;
    private final OntolociDAO persistence = OntolociInMemoryDAO.instance();

    /**
     * Factory method that creates a WorkerExecutor instance from a Worker instance.
     * @param worker from which to create the new WorkerExecutor.
     * @return the new WorkerExecutor instance.
     */
    public static WorkerExecutor from(Worker worker, RepositoryProvider repositoryProvider) {
        LOGGER.debug("Static factory creating a new worker executor for " + worker+ " and "+ repositoryProvider);
        return new WorkerExecutor(worker,repositoryProvider);
    }

    public static WorkerExecutor from(Worker worker) {
        LOGGER.debug("Static factory creating a new worker executor for " + worker);
        return new WorkerExecutor(worker,GitHubRepositoryProvider.empty());
    }
    
    /**
     * Main constructor for the worker executor class. This is intended for dependency injection.
     * @param worker to execute the builds.
     */
    private WorkerExecutor(final Worker worker, RepositoryProvider repositoryProvider) {
        this.worker = worker;
        this.repositoryProvider = repositoryProvider;
    }

    /**
     * This method receives an empty build that will be filled calling the appropriate Hub implementation,
     * who also will create a check run.
     * Once the build is filled, this worker will delegate the work to the sequential worker, who will
     * perform the validation of the build.
     * Then, the check run will be updated with the validation results.
     * Finally, the build results will be stored in the persist layer.
     *
     * @param build to allocate in the worker.
     * @return build result
     */
    @Override
    public BuildResult executeBuild(Build build) {
        LOGGER.debug("Executing build: " + build);
        if(build.getMetadata().size()<=0)
            return null;
        // 1. Create a Hub instance
        HubImplementation ontolocyHub = new HubImplementation(repositoryProvider);
        // 2. Transform the current build to a HubBuild
        HubBuild hubBuild = build.toHubBuild();
        // 3. Add the tests to the build
        hubBuild = ontolocyHub.fillBuild(hubBuild);
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


    /**
     * Delegates the work to the sequential worker to perform the validation
     * in case the build result has not previous exceptions during the hub tasks.
     * Otherwise, it returns and empty build result.
     *
     * @param build to be executed
     * @return build result after the execution or empty build
     */
    private BuildResult executeWorker(Build build){
        if(hasExceptions(build)){
            return BuildResult.from(build.getId(),build.getMetadata(),BuildResultStatus.CANCELLED,new ArrayList<>());
        }

        BuildResult buildResult;
        try {

             buildResult = this.worker.executeBuild(build);

        }catch (RuntimeException e){

            LOGGER.error(String.format("ERROR while trying to validate build=[%s]",build));
            Map<String,String> metadata = fillMetadataException(build.getMetadata(),"ValidationError","Error during validation");
            buildResult = BuildResult.from(build.getId(),metadata,BuildResultStatus.CANCELLED,new ArrayList<>());

        }

        return buildResult;
    }

    private Map<String,String> fillMetadataException(Map<String,String> metadata,String checkTitle,String checkBody){
        Map<String,String> newMap = new HashMap<>(metadata);
        newMap.put("exceptions","true");
        newMap.put("checkTitle",checkTitle);
        newMap.put("checkBody",checkBody);
        newMap.put("execution_date",String.valueOf(System.currentTimeMillis()));
        return newMap;
    }

    /**
     * Calls the Hub in order to update the check run once the validation is performed.
     * @param ontolocyHub hub
     * @param buildResult build result
     */
    private void updateCheckRun(HubImplementation ontolocyHub, BuildResult buildResult) {
        String conclusion = getConclusion(buildResult);
        String output = getOutput(buildResult);
        ontolocyHub.updateCheckRun(conclusion,output);
    }

    /**
     * Stores build results in the persistence layer
     * @param buildResult to be stored
     */
    private void persist(BuildResult buildResult) {
        LOGGER.debug("INTERNAL validation finished, storing results in persistence layer");
        PersistedBuildResult b =BuildResult.toPersistedBuildResult(buildResult);
        persistence.save(b);
    }

    /**
     * Gets the conclusion param for the check run update.
     * The conclusion may be:
     *
     *  - SUCCESS when the build finishes and the result is positive.
     *  - FAILURE when the build finishes and the result is not positive.
     *
     * @param buildResult
     * @return conclusion as a string
     */
    private String getConclusion(BuildResult buildResult) {
        return buildResult.getStatus().getValue();
    }

    /**
     * Gets the output param for the check run update.
     * This param provides descriptive details about the run.
     *
     * @param buildResult
     * @return output as a string
     */
    private String getOutput(BuildResult buildResult) {
        String title = getTilte(buildResult);
        String body = getBody(buildResult);
        return "{\"title\":\""+title+"\",\"summary\":\""+body+"\"}";
    }

    /**
     * Gets the title for the check run output param.
     * This param is fetched from the build metadata.
     *
     * @param buildResult
     * @return title as a string
     */
    private String getTilte(BuildResult buildResult) {
        return buildResult.getMetadata().get("checkTitle");
    }

    /**
     * Gets the body for the check run output param.
     * The body content will be a markdown string with the results of the build
     * in case the build result has not previous exceptions during the hub tasks.
     * Otherwise, the body content will be fetched from the build metadata
     *
     * @param buildResult
     * @return body as a string
     */
    private String getBody(BuildResult buildResult) {
        return !hasExceptions(buildResult) ?
                MarkdownUtils.getMarkDownFromTests(buildResult.getTestCaseResults()) :
                buildResult.getMetadata().get("checkBody");
    }

    /**
     * Checks if the build has suffered any exception during the hub tasks
     *
     * @param build
     * @return true if it has been any exception, false otherwise
     */
    private boolean hasExceptions(Build build){
        return build.getMetadata().get("exceptions").equals("true");
    }

    /**
     * Checks if the build result has suffered any exception during the hub tasks
     *
     * @param buildResult
     * @return true if it has been any exception, false otherwise
     */
    private boolean hasExceptions(BuildResult buildResult){
        return buildResult.getMetadata().get("exceptions").equals("true");
    }

}
