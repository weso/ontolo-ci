package es.weso.ontoloci.listener;


import es.weso.ontoloci.hub.exceptions.EmptyContentFileException;
import es.weso.ontoloci.scheduler.SchedulerImpl;
import es.weso.ontoloci.worker.build.Build;
import fansi.Str;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/github")
public class GitHubRestListener implements RepositoryRestListener{

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRestListener.class);

    private static final String GITHUB_PUSH_EVENT = "push";
    private static final String GITHUB_PULL_REQUEST_EVENT = "pull_request";

    public static void main(String... args) {
        SpringApplication.run(GitHubRestListener.class, args);
    }

    @RequestMapping("/")
    public void listen(@RequestHeader("X-GitHub-Event") String event, @RequestBody Map<String, Object> payload) {
        // We only listen to these two type of events.
        LOGGER.debug("GITHUB-EVENT- "+ event);
        if(Objects.equals(event, GITHUB_PUSH_EVENT))
            handlePush(payload);
        if(Objects.equals(event, GITHUB_PULL_REQUEST_EVENT))
            handlePullRequest(payload);
    }



    private void handlePush(Map<String, Object> payload) {
            Map<String, Object> repositoryData = (Map<String, Object>) payload.get("repository");
            Map<String, Object> ownerData = (Map<String, Object>) repositoryData.get("owner");
            ArrayList<Map<String, Object>> commitData = (ArrayList<Map<String, Object>>) payload.get("commits");

            // We don´t want to do nothing if there is nothing to commit
            // This usually happens with the push of a new branch
            if(commitData.size()<=0)
                return;

            final Build build = Build.from();
            // Parse the content and create the test cases array.
            final String owner = (String) ownerData.get("name");
            final String repo = (String) repositoryData.get("name");
            final String branch = ((String) payload.get("ref")).split("refs/heads/")[1];
            final String commit = (String) commitData.get(0).get("id");
            final String commitId = String.valueOf(commit).substring(0,6);
            final String commitName = (String) commitData.get(0).get("message");
            final String prNumber = "none";

            // Add the metadata.
            Map<String, String> metadata = fillMetadata(owner,repo,branch,commit,commitId,commitName,prNumber);
            // We set the metadata.
            build.setMetadata(metadata);

            // We don´t want to handle a push if it´s contained in a PR because it will be handle by the PR
            if(!isInPullRequest(owner,repo,commit)){
                // Instantiate the scheduler.
                SchedulerImpl.getInstance().scheduleBuild(build);
            }

    }


    private void handlePullRequest(Map<String, Object> payload) {

        if(!payload.get("action").equals("closed")) {
            Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
            Map<String, Object> headData = (Map<String, Object>) pullRequest.get("head");
            Map<String, Object> repoData = (Map<String, Object>) headData.get("repo");
            Map<String, Object> ownerData = (Map<String, Object>) repoData.get("owner");

            final Build build = Build.from();
            // Parse the content and create the test cases array.
            final String owner = (String) ownerData.get("login");
            final String repo = (String) repoData.get("name");
            final String branch = (String) headData.get("ref");
            final String commit = (String) headData.get("sha");
            final String commitId = String.valueOf(commit).substring(0, 6);
            final String commitName = (String) pullRequest.get("title");
            final String prNumber = String.valueOf(payload.get("number"));

            // Add the metadata.
            Map<String, String> metadata = fillMetadata(owner,repo,branch,commit,commitId,commitName,prNumber);
            // We set the metadata.
            build.setMetadata(metadata);

            SchedulerImpl.getInstance().scheduleBuild(build);

        }
    }


    private Map<String, String> fillMetadata(String owner,String repo,String branch,String commit,String commitId,String commitName,String prNumber){
        Map<String, String> metadata = new HashMap<>();
        metadata.put("owner", owner);
        metadata.put("repo", repo);
        metadata.put("branch", branch);
        metadata.put("commit", commit);
        metadata.put("commitId", commitId);
        metadata.put("commitName", commitName);
        metadata.put("prNumber", prNumber);
        return metadata;
    }


    private boolean isInPullRequest(String owner, String repo, String commit){
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.github.com/repos/"+owner+"/"+repo+"/commits/"+commit+"/pulls");
        httpGet.addHeader("Accept", "application/vnd.github.groot-preview+json");

        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    String result =  IOUtils.toString(instream, "UTF-8");
                    if(result.startsWith("[") && result.endsWith("]") && result.length()==2){ // [] ->No PRs
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
