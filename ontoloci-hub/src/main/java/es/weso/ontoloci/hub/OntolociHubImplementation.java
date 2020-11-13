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


    public void saveInstallation(String owner, String code){
        GitHubRepositoryProvider gh = GitHubRepositoryProvider.empty();
        String auth = gh.getPersonalAccessToken(code);
        installations.put(owner,gh.getInstallationId(auth));
    }




    public static void main(String[] args) throws Exception {
       // OntolociHubImplementation o = new OntolociHubImplementation();
       // o.saveInstallation("mistermbot","3147d7e5513b16e1be87");
        GitHubRepositoryProvider gh = GitHubRepositoryProvider.empty();
        String token = gh.authenticateByInstallation();
        System.out.print(token);
    }








}
