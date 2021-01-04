package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import fansi.Str;
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
    final GitHubRepositoryProvider gitHubService;

    private Map<String,String> installations;

    private String currentOwner;
    private String currentRepo;
    private String currentCommit;
    private String currentCheckRunId;

    public OntolociHubImplementation() {

        LOGGER.debug(
                "NEW Creating a new OntolociHubImplementation from the public constructor"
        );

        gitHubService = GitHubRepositoryProvider.empty();
        installations = new HashMap<>();
    }


    @Override
    public HubBuild addTestsToBuild(HubBuild hubBuild) {

        Map<String,String> metadata = new HashMap<>(hubBuild.getMetadata());

        // Parse the data from the build.
        currentOwner = hubBuild.getMetadata().get("owner");
        currentRepo = hubBuild.getMetadata().get("repo");
        currentCommit = hubBuild.getMetadata().get("commit");

        LOGGER.debug(
                "Calling the GitHub Service with [%s,%s,%s] for the collection of HubTestCase from the OntolociHubImplementation",
                currentOwner,
                currentRepo,
                currentCommit
        );

        // Create the check run
        currentCheckRunId = gitHubService.createCheckRun(currentOwner,currentRepo,currentCommit);

        try {
            final Collection<HubTestCase> testsCases = gitHubService.getTestCases(currentOwner, currentRepo, currentCommit);

            // Populate the hub build with the computed test cases.
            hubBuild.setTestCases(testsCases);
            metadata.put("exceptions","false");

        }catch (FileNotFoundException e) {
            LOGGER.error(String.format("ERROR while getting any file at getTestCases from GitHubRepositoryProvider: %s",e.getMessage()));
            metadata.put("exceptions","true");
            metadata.put("checkTitle","FileNotFound");
            metadata.put("checkBody","Any file was not found");
            metadata.put("execution_date",String.valueOf(System.currentTimeMillis()));
        }
        catch (Exception e) {
            LOGGER.error(String.format("ERROR while getting the HubTestCases at getTestCases from GitHubRepositoryProvider: %s",e.getMessage()));
            metadata.put("exception","true");
            metadata.put("checkTitle","Exception");
            metadata.put("checkBody","Something went wrong");
            metadata.put("execution_date",String.valueOf(System.currentTimeMillis()));
        }

        hubBuild.setMetadata(metadata);
        return hubBuild;
    }

    @Override
    public void updateCheckRun(String conclusion,String output){
        gitHubService.updateCheckRun(currentCheckRunId,currentOwner,currentRepo,conclusion,output);
    }












}
