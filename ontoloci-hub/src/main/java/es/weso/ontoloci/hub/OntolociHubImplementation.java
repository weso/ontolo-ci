package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;


import java.util.Collection;

public class OntolociHubImplementation implements OntolociHub {

    @Override
    public HubBuild addTestsToBuild(HubBuild hubBuild) {
        String owner = hubBuild.getMetadata().get("owner");
        String repo = hubBuild.getMetadata().get("repo");
        String branch = hubBuild.getMetadata().get("branch");

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        Collection<HubTestCase> testCases = gitHubService.getTestCases(owner,repo,branch);
        hubBuild.setTestCases(testCases);
        return hubBuild;
    }
}
