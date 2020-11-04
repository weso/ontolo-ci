package es.weso.ontoloci.listener;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.ontoloci.scheduler.Scheduler;
import es.weso.ontoloci.worker.build.Build;
import org.apache.jena.base.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/github")
public class GitHubRestListener {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRestListener.class);

    private static final String REPO_KEY = "repository";
    private static final String OWNER_KEY = "owner";
    private static final String BRANCH_KEY = "branch";

    @RequestMapping(value = "/push",method = RequestMethod.POST)
    public void pushListener(@RequestBody Map<String, Object> payload) {
            Map<String, Object> repositoryData = (Map<String, Object>) payload.get("repository");
            Map<String, Object> ownerData = (Map<String, Object>) repositoryData.get("owner");
            ArrayList<Map<String, Object>> commitData = (ArrayList<Map<String, Object>>) payload.get("commits");

            final Build build = Build.from();
            // Parse the content and create the test cases array.
            final String owner = (String) ownerData.get("name");
            final String repo = (String) repositoryData.get("name");
            final String branch = "main";
            final String commmitId = String.valueOf(commitData.get(0).get("id")).substring(0,6);
            final String commmitName = (String) commitData.get(0).get("message");

            // Add the metadata.
            Map<String, String> metadata = new HashMap<>();
            metadata.put("owner", owner);
            metadata.put("repo", repo);
            metadata.put("branch", branch);
            metadata.put("commmitId", commmitId);
            metadata.put("commmitName", commmitName);

            // We set the metadata.
            build.setMetadata(metadata);

            // Instantiate the scheduler.
            Scheduler.getInstance().scheduleBuild(build);

            System.out.println("PUSH WORKS!");
    }

    @RequestMapping(value = "/pull_request",method = RequestMethod.POST)
    public void pullRequestListener(@RequestBody Map<String, Object> payload) {
        System.out.println("PULL REQUEST WORKS!");
    }
}
