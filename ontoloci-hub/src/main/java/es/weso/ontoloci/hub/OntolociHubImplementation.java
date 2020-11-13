package es.weso.ontoloci.hub;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import es.weso.ontoloci.hub.utils.KeyUtils;
import fansi.Str;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.FileUtils;
import org.apache.jena.base.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        String installationId = gitHubService.getInstallationId(currentOwner);
        String token = gitHubService.authenticateByInstallation(installationId);
        currentCheckRunId = gitHubService.createCheckRun(token,currentOwner,currentRepo,currentCommit);

        // Create the tests collection from the owner+repo+branch.
        final Collection<HubTestCase> testsCases = gitHubService.getTestCases(currentOwner,currentRepo,currentCommit);

        // Populate the hub build with the computed test cases.
        hubBuild.setTestCases(testsCases);
        return hubBuild;
    }

    @Override
    public void updateCheckRun(boolean hasPassed){
        String installationId = gitHubService.getInstallationId(currentOwner);
        String token = gitHubService.authenticateByInstallation(installationId);
        gitHubService.updateCheckRun(token,currentCheckRunId,hasPassed,currentOwner,currentRepo);
    }










}
