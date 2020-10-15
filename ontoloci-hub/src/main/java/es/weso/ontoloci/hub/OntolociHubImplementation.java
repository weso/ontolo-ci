package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.test.TestCase;

import java.util.Collection;

public class OntolociHubImplementation implements OntolociHub {

    @Override
    public Build addTestsToBuild(Build build) {
        String owner = build.getMetadata().get("owner");
        String repo = build.getMetadata().get("repo");
        String branch = build.getMetadata().get("branch");

        GitHubRepositoryProvider gitHubService = GitHubRepositoryProvider.empty();
        Collection<TestCase> testCases = gitHubService.getTestCases(owner,repo,branch);
        build.setTestCases(testCases);
        return build;
    }
}
