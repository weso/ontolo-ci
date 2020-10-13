package es.weso.ontoloci.listener;

import es.weso.ontoloci.scheduler.Scheduler;
import es.weso.ontoloci.worker.build.Build;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/github")
public class GitHubRestListener {

    private static final String GITHUB_PUSH_EVENT = "push";
    private static final String GITHUB_PULL_REQUEST_EVENT = "pull_request";

    @RequestMapping("/")
    public void listen(@RequestHeader("X-GitHub-Event") String event, @RequestBody Map<String, Object> payload) {
        // We only listen to these two type of events.
        if(Objects.equals(event, GITHUB_PUSH_EVENT) && Objects.equals(event, GITHUB_PULL_REQUEST_EVENT)) {

            // Parse the content and create the test cases array.

            // Instantiate the scheduler.
            Scheduler.getInstance().scheduleBuild(Build.from());
        }
    }
}
