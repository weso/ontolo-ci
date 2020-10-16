package es.weso.ontoloci.listener;

import es.weso.ontoloci.scheduler.Scheduler;
import es.weso.ontoloci.worker.build.Build;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/github")
public class GitHubRestListener {

    private static final String GITHUB_PUSH_EVENT = "push";
    private static final String GITHUB_PULL_REQUEST_EVENT = "pull_request";
    private static final String REPO_KEY = "repository";
    private static final String OWNER_KEY = "owner";
    private static final String BRANCH_KEY = "branch";

    @RequestMapping("/")
    public void listen(@RequestHeader("X-GitHub-Event") String event, @RequestBody Map<String, Object> payload) {
        // We only listen to these two type of events.
        if(Objects.equals(event, GITHUB_PUSH_EVENT) && Objects.equals(event, GITHUB_PULL_REQUEST_EVENT)) {
            final Build build = Build.from();
            // Parse the content and create the test cases array.
            final String owner = "";
            final String repo = "";
            final String branch = "";

            // Add the metadata.
            Map<String, String> metadata = new HashMap<>();
            metadata.put("owner", payload.get(OWNER_KEY).toString());
            metadata.put("repo", payload.get(REPO_KEY).toString());
            metadata.put("branch", payload.get(BRANCH_KEY).toString());

            // We set the metadata.
            build.setMetadata(metadata);

            // Instantiate the scheduler.
            Scheduler.getInstance().scheduleBuild(build);
        }
    }
}
