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
            final String branch = ((String) payload.get("ref")).split("refs/heads/")[1];
            final String commit = (String) commitData.get(0).get("id");
            final String commmitId = String.valueOf(commit).substring(0,6);
            final String commmitName = (String) commitData.get(0).get("message");

            // Add the metadata.
            Map<String, String> metadata = new HashMap<>();
            metadata.put("owner", owner);
            metadata.put("repo", repo);
            metadata.put("branch", branch);
            metadata.put("commit", commit);
            metadata.put("commitId", commmitId);
            metadata.put("commitName", commmitName);

            // We set the metadata.
            build.setMetadata(metadata);

            // Instantiate the scheduler.
            Scheduler.getInstance().scheduleBuild(build);

    }

    @RequestMapping(value = "/pull_request",method = RequestMethod.POST)
    public void pullRequestListener(@RequestBody Map<String, Object> payload) {

        if(!payload.get("action").equals("closed")) {
            Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
            Map<String, Object> userData = (Map<String, Object>) pullRequest.get("user");
            Map<String, Object> headData = (Map<String, Object>) pullRequest.get("head");
            Map<String, Object> repoData = (Map<String, Object>) headData.get("repo");

            final Build build = Build.from();
            // Parse the content and create the test cases array.
            final String owner = (String) userData.get("login");
            final String repo = (String) repoData.get("name");
            final String branch = (String) headData.get("ref");
            final String commit = (String) headData.get("sha");
            final String commmitId = String.valueOf(commit).substring(0, 6);
            final String commmitName = (String) pullRequest.get("title");
            final String prNumber = String.valueOf(payload.get("number"));

            // Add the metadata.
            Map<String, String> metadata = new HashMap<>();
            metadata.put("owner", owner);
            metadata.put("repo", repo);
            metadata.put("branch", branch);
            metadata.put("commit", commit);
            metadata.put("commitId", commmitId);
            metadata.put("commitName", commmitName);
            metadata.put("prNumber", prNumber);

            // We set the metadata.
            build.setMetadata(metadata);

            // Instantiate the scheduler.
            Scheduler.getInstance().scheduleBuild(build);
        }
    }
}
