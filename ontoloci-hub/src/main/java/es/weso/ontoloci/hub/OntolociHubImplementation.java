package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * @author Pablo Menéndez Suárez
 */
public class OntolociHubImplementation implements OntolociHub {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(OntolociHubImplementation.class);

    // Instantiate the github service.
    final GitHubRepositoryProvider gitHubProvider;

    private String currentOwner;
    private String currentRepo;
    private String currentCommit;
    private String currentCheckRunId;

    public OntolociHubImplementation() {

        LOGGER.debug("Creating a new OntolociHubImplementation from the public constructor");

        gitHubProvider = GitHubRepositoryProvider.empty();
    }

    /**
     * Add the tests to an empty build object.
     *
     * @param hubBuild to populate with test cases.
     * @return the populated hub build.
     */
    @Override
    public HubBuild addTestsToBuild(HubBuild hubBuild) {

        // Parse the data from the build.
        currentOwner  = hubBuild.getMetadata().get("owner");
        currentRepo   = hubBuild.getMetadata().get("repo");
        currentCommit = hubBuild.getMetadata().get("commit");

        LOGGER.debug("Adding test to Build for [%s,%s,%s]", currentOwner, currentRepo,currentCommit);

        // Create the check run
        currentCheckRunId = createGitHubCheckRun();

        return fillHBuildFromGitHub(hubBuild);
    }

    /**
     * Updates an existing checkrun with a new status and a output message
     *
     * @param conclusion    new status
     * @param output        output message
     */
    @Override
    public void updateCheckRun(String conclusion,String output){
        LOGGER.debug("Updating GitHub ChekRun=[%s] for [%s,%s,%s] with status=[%s] and msg=[%s]",currentCheckRunId,currentOwner,currentRepo,currentCommit,conclusion,output);
        gitHubProvider.updateCheckRun(currentCheckRunId,currentOwner,currentRepo,conclusion,output);
    }

    /**
     * Creates a new GitHub checkrun for a specific commit of a GitHub repository
     * @return  checkrunId
     */
    private String createGitHubCheckRun(){
        LOGGER.debug("Creating GitHub ChekRun for [%s,%s,%s]",currentOwner,currentRepo,currentCommit);
        return gitHubProvider.createCheckRun(currentOwner,currentRepo,currentCommit);
    }

    /**
     * Calls the gitHubProvider in order to obtain the test cases and add them to the hubbuild.
     * If something goes wrong, adds the appropriate exception messages to the hubbuild metadata.
     *
     * @param hubBuild
     * @return hubBuild
     */
    private HubBuild fillHBuildFromGitHub(HubBuild hubBuild){
        LOGGER.debug("Calling the GitHub provider with [%s,%s,%s] for the collection of HubTestCase from the OntolociHubImplementation", currentOwner, currentRepo,currentCommit);

        Map<String,String> metadata = new HashMap<>(hubBuild.getMetadata());

        try {
            final Collection<HubTestCase> testsCases = gitHubProvider.getTestCases(currentOwner, currentRepo, currentCommit);
            // Populate the hub build with the computed test cases.
            hubBuild.setTestCases(testsCases);
            metadata.put("exceptions","false");

        }catch (FileNotFoundException e) {
            LOGGER.error(String.format("ERROR while getting any file at getTestCases from GitHubRepositoryProvider: %s",e.getMessage()));
            metadata = getFilleNotFoundMetadata();
        }
        catch (Exception e) {
            LOGGER.error(String.format("ERROR while getting the HubTestCases at getTestCases from GitHubRepositoryProvider: %s",e.getMessage()));
            metadata = getExceptionMetadata();
        }

        hubBuild.setMetadata(metadata);
        return hubBuild;
    }

    /**
     * Gets a metadata map with the FileNotFound exception attributes
     * @return metadata
     */
    private Map<String,String> getFilleNotFoundMetadata(){
        return fillMetadataException("FileNotFound","Some file was not found");
    }

    /**
     * Gets a metadata map with the common exception attributes
     * @return metadata
     */
    private Map<String,String> getExceptionMetadata(){
        return fillMetadataException("Exception","Something went wrong");
    }

    /**
     * Creates a metadata map with a specific exceptions fields.
     *
     * @param checkTitle    title for the checkrun
     * @param checkBody     body for the checkrun
     *
     * @return metadata
     */
    private Map<String,String> fillMetadataException(String checkTitle,String checkBody){
        Map<String,String> metadata = new HashMap<>();
        metadata.put("exceptions","true");
        metadata.put("checkTitle",checkTitle);
        metadata.put("checkBody",checkBody);
        metadata.put("execution_date",String.valueOf(System.currentTimeMillis()));
        return metadata;
    }












}
