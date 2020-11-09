package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Collection;

/**
 *
 * @author Pablo Menéndez Suárez
 */
public class OntolociHubImplementation implements OntolociHub {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(OntolociHubImplementation.class);

    // Instantiate the github service.
    final GitHubRepositoryProvider gitHubService;

    public OntolociHubImplementation() {

        LOGGER.debug(
                "NEW Creating a new OntolociHubImplementation from the public constructor"
        );

        gitHubService = GitHubRepositoryProvider.empty();
    }


    @Override
    public HubBuild addTestsToBuild(HubBuild hubBuild) {

        // Parse the data from the build.
        final String owner = hubBuild.getMetadata().get("owner");
        final String repo = hubBuild.getMetadata().get("repo");
        final String commit = hubBuild.getMetadata().get("commit");

        LOGGER.debug(
                "Calling the GitHub Service with [%s,%s,%s] for the collection of HubTestCase from the OntolociHubImplementation",
                owner,
                repo,
                commit
        );

        // Create the tests collection from the owner+repo+branch.
        final Collection<HubTestCase> testsCases = gitHubService.getTestCases(owner,repo,commit);

        // Populate the hub build with the computed test cases.
        hubBuild.setTestCases(testsCases);
        return hubBuild;
    }
}
