package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.exceptions.EmptyContentFileException;
import es.weso.ontoloci.hub.repository.RepositoryProvider;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * OntolociHub implementation
 * @author Pablo Menéndez Suárez
 */
public class HubImplementation implements Hub {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(HubImplementation.class);

    // Instantiate the github service.
    final RepositoryProvider repositoryProvider;

    private String currentOwner;
    private String currentRepo;
    private String currentCommit;
    private String currentCheckRunId;

    public HubImplementation(RepositoryProvider repositoryProvider) {

        LOGGER.debug("Creating a new OntolociHubImplementation from the public constructor");
        LOGGER.debug("Repository Provider: "+repositoryProvider.toString());
        this.repositoryProvider = repositoryProvider;

    }

    /**
     * Add the tests to an empty build object.
     *
     * @param hubBuild to populate with test cases.
     * @return the populated hub build.
     */
    @Override
    public HubBuild fillBuild(HubBuild hubBuild) {

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
        try {
            repositoryProvider.updateCheckRun(currentCheckRunId,currentOwner,currentRepo,conclusion,output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new GitHub checkrun for a specific commit of a GitHub repository
     * @return  checkrunId
     */
    private String createGitHubCheckRun(){
        LOGGER.debug("Creating GitHub ChekRun for [%s,%s,%s]",currentOwner,currentRepo,currentCommit);
        try {
            return repositoryProvider.createCheckRun(currentOwner,currentRepo,currentCommit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            final Collection<HubTestCase> testsCases = repositoryProvider.getTestCases(currentOwner, currentRepo, currentCommit);
            // Populate the hub build with the computed test cases.
            hubBuild.setTestCases(testsCases);
            metadata.put("exceptions","false");

        }catch (FileNotFoundException e) {
            LOGGER.error(String.format("ERROR while getting any file at getTestCases from RepositoryProvider: %s", repositoryProvider));
            addFilleNotFoundMetadata(metadata);
        }
        catch (EmptyContentFileException e) {
            LOGGER.error(String.format("ERROR while getting the HubTestCases at getTestCases from RepositoryProvider: %s",repositoryProvider));
            addEmptyContentFilleMetadata(metadata);
        }
        catch (Exception e) {
            LOGGER.error(String.format("ERROR while getting the HubTestCases at getTestCases from RepositoryProvider: %s",repositoryProvider));
            addExceptionMetadata(metadata);
        }

        hubBuild.setMetadata(metadata);
        return hubBuild;
    }

    /**
     * Adds FileNotFound exception attributes to a metadata map
     * @param metadata map
     */
    private void addFilleNotFoundMetadata(Map<String,String> metadata){
        fillMetadataException(metadata,"FileNotFound","Some file was not found");
    }

    private void addEmptyContentFilleMetadata(Map<String,String> metadata){
        fillMetadataException(metadata,"EmptyContentFile","Some files have not content");
    }

    /**
     * Adds common exception attributes to a metadata map
     * @param metadata map
     */
    private void addExceptionMetadata(Map<String,String> metadata){
        fillMetadataException(metadata,"Exception","Something went wrong");
    }

    /**
     * Fills a metadata map with a specific exceptions fields.
     *
     * @param metadata map
     * @param checkTitle    title for the checkrun
     * @param checkBody     body for the checkrun
     *
     */
    private void fillMetadataException(Map<String,String> metadata,String checkTitle,String checkBody){
        metadata.put("exceptions","true");
        metadata.put("checkTitle",checkTitle);
        metadata.put("checkBody",checkBody);
        metadata.put("execution_date",String.valueOf(System.currentTimeMillis()));
    }












}
