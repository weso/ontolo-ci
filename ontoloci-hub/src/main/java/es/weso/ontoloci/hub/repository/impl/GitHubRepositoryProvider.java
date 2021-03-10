package es.weso.ontoloci.hub.repository.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import es.weso.ontoloci.hub.exceptions.EmptyContentFileException;
import es.weso.ontoloci.hub.manifest.Manifest;
import es.weso.ontoloci.hub.manifest.ManifestEntry;
import es.weso.ontoloci.hub.repository.RepositoryConfiguration;
import es.weso.ontoloci.hub.repository.RepositoryProvider;
import es.weso.ontoloci.hub.test.HubTestCase;
import es.weso.ontoloci.hub.utils.KeyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * This class implements the methods of the RepositoryProvider interface for the repository provider GitHub.
 * It contains all the needed methods to collect data and handle checkruns on GitHub.
 *
 * @author Pablo Menéndez
 */
public class GitHubRepositoryProvider implements RepositoryProvider {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRepositoryProvider.class);

    private final static String GITHUB_RAW_REQUEST      =   "https://raw.githubusercontent.com/";
    private final static String GITHUB_API_REQUEST      =   "https://api.github.com/";
    private final static String INSTALLATION_REQUEST    =   "https://api.github.com/users/USERNAME/installation";

    private final static String YAML_FILE_NAME          =   ".oci.yml";
    private final static String SLASH                   =   "/";

    private final ObjectMapper yamlMapper;
    private final ObjectMapper jsonMapper;

    /**
     * Creates an empty default github repository provider object.
     * It initializes both YAML and JSON mappers to the default values.
     *
     * @return a new GitHubRepositoryProvider object with default configurations.
     */
    public static GitHubRepositoryProvider empty() {

        LOGGER.debug(String.format("NEW Creating new GitHubRepositoryProvider from the static factory with default YAML and JSON mappers"));

        return new GitHubRepositoryProvider();
    }

    /**
     * Creates a nre GitHubRepositoryProvider with the given object mappers both for YAML and JSON.
     *
     * @param yamlMapper to assign tho the object.
     * @param jsonMapper to assign to the object
     * @return a new GitHubRepositoryProvider object with the given configuration.
     */
    public static GitHubRepositoryProvider with(final ObjectMapper yamlMapper, final ObjectMapper jsonMapper) {

        LOGGER.debug(String.format("NEW Creating new GitHubRepositoryProvider from the static factory with a custom YAML and JSON mappers"));

        return new GitHubRepositoryProvider(yamlMapper, jsonMapper);
    }

    /**
     * GitHubRepositoryProvider Private Constructor with empty mappers
     */
    private GitHubRepositoryProvider() {

        LOGGER.debug(String.format("NEW Creating new GitHubRepositoryProvider from the public constructor with default YAML and JSON mappers"));

        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.jsonMapper = new ObjectMapper(new JsonFactory());
    }

    /**
     * GitHubRepositoryProvider Private Constructor with mappers as a param
     */
    private GitHubRepositoryProvider(final ObjectMapper yamlMapper, final ObjectMapper jsonMapper) {

        LOGGER.debug(String.format("NEW Creating new GitHubRepositoryProvider from the public constructor with a custom YAML and JSON mappers"));

        this.yamlMapper = yamlMapper;
        this.jsonMapper = jsonMapper;
    }



    /**
     * Gets a collection of test cases from a specific commit of a GitHub repository.
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param commit                of the repository
     *
     * @return test cases
     */
    @Override
    public Collection<HubTestCase> getTestCases(final String owner, final String repo,final String commit) throws IOException {

        LOGGER.debug(String.format("GET Computing the collection of HubTestCase for user=[%s], repo =[%s] and commit=[%s]", owner, repo, commit));
        // Result collection, initialized to empty one so not null is returned.
        final Collection<HubTestCase> hubTestCases = new ArrayList<>();
        // 1. Get the repository configuration file.
        final RepositoryConfiguration repositoryConfig = getRepositoryConfiguration(getYAMLPath(owner,repo,commit));
        // 2. Parse the repository configuration file and create a manifest object
        final Manifest manifest = getManifest(getManifestPath(owner,repo,commit,repositoryConfig));
        // 3. Get the ontology folder
        final String ontologyFolder = repositoryConfig.getOntologyFolder();
        // 4. Get the tests folder
        final String testsFolder = repositoryConfig.getTestFolder();
        // 5. Get collection of generated test cases from the manifest file.
        final Collection<HubTestCase> parsedTestCases = getTestCasesFromManifest(owner,repo,commit,ontologyFolder,testsFolder,manifest);
        LOGGER.debug(String.format("INTERNAL parsed test cases [%s]",parsedTestCases.size()));
        // 6. Add all the test cases to the result collection.
        hubTestCases.addAll(parsedTestCases);
        // 7. Finally, return the test cases
        return hubTestCases;
    }

    /**
     * Creates a checkrun in a specific commit of a GitHub repository.
     *
     * @param owner     of the repository
     * @param repo      name of the repository
     * @param commit    sha of the commit
     *
     * @return checkRunId
     */
    @Override
    public String createCheckRun(String owner,String repo,String commit) throws IOException {

        LOGGER.debug( String.format("Creating CheckRun  for user=[%s], repo =[%s] and commit =[%s] ",owner,repo,commit));

        // 1. Create the HttpClient
        HttpClient httpclient = HttpClients.createDefault();
        // 2. Set the request path
        String path = getCheckRunsPath(owner,repo);
        // 3. Authenticate the user
        String authToken = authenticate(owner);
        LOGGER.debug( String.format("AuthToken=[%s]",authToken));
        // 4. Create the appropriate HTTP method for the request
        HttpPost httppost = getGitHubPostAuth(path,authToken);
        // 5. Set the request params
        httppost  = addCreateCheckRunParams(httppost,commit);
        // 6. Perform the request
        String response = executeRequest(httpclient,httppost);
        // 7. Obtain the checkRunId from the response
        String checkRunId =  getCheckRunIdFromResponse(response);


        LOGGER.debug( String.format("Created CheckRun for user=[%s], repo =[%s], commit =[%s] and checkRunId=[%s]",owner,repo,commit,checkRunId));

        return checkRunId;
    }


    /**
     * Updates an existing checkrun in a GitHub repository with a new status.
     *
     * @param checkRunId            id of the checkRun
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param conclusion            new status of the checkrun
     * @param output                message
     */
    @Override
    public String updateCheckRun(String checkRunId, String owner, String repo, String conclusion,String output) throws IOException {

        LOGGER.debug( String.format("Updating CheckRun = [%s] for user=[%s] and repo =[%s] ",checkRunId,owner,repo));

        // 1. Create the HttpClient
        HttpClient httpclient = HttpClients.createDefault();
        // 2. Set the request path
        String path = getUpdateCheckRunPath(owner,repo,checkRunId);
        // 3. Authenticate the user
        String authToken = authenticate(owner);
        // 4. Create the appropriate HTTP method for the request
        HttpPatch httpatch = getGitHubPatchAuth(path,authToken);
        // 5. Set the request params
        httpatch  = addUpdateCheckRunParams(httpatch,conclusion,output);
        // 6. Perform the request
        String result = executeRequest(httpclient,httpatch);

        LOGGER.debug( String.format("CheckRun updated = [%s] for user=[%s] and repo =[%s] ",checkRunId,owner,repo));

        return result;
    }

    /**
     * Authenticates a GitHub user via Installation Id
     *
     * @param user to be authenticated
     * @return authentication token
     */
    private String authenticate(String user) throws IOException {
        String installationId = getInstallationId(user);
        return authenticateByInstallation(installationId);
    }

    /**
     * Obtains the GitHub Installation Id from a GitHub user
     *
     * @param user   to get their Installation Id
     * @return installationId
     */
    private String getInstallationId(String user) throws IOException {

        LOGGER.debug( String.format("Getting InstallationId for user=[%s] ",user));

        // Create the HttpClient
        HttpClient httpclient = HttpClients.createDefault();
        // Set the specific path
        String path = INSTALLATION_REQUEST.replace("USERNAME",user);
        // Create the appropriate HTTP method for the request
        HttpGet httpGet = getGitHubGetAuth(path);
        // Perform the request
        String response = executeRequest(httpclient,httpGet);
        LOGGER.debug( String.format("Response=[%s] ",response));
        // Obtain the installationId from the response
        String installationId = getInstallationIdFromResponse(response,user);

        LOGGER.debug( String.format("InstallationId obtained for user=[%s] ",user));
        LOGGER.debug( String.format("InstallationId obtained=[%s] ",installationId));

        return installationId;
    }

    /**
     * Authenticates a user by their installationId
     *
     * @param installationId
     * @return authorization token
     */
    private String authenticateByInstallation(String installationId) throws IOException {

        LOGGER.debug( String.format("Authenticating installationId = [%s]",installationId));

        // 1. Create the HttpClient
        HttpClient httpclient = HttpClients.createDefault();
        // 2. Set the request path
        String path = getAuthenticationByInstallationPath(installationId);
        // 3. Create the appropriate HTTP method for the request
        HttpPost httppost = getBearerGitHubPost(path);
        // 4. Perform the request
        String response = executeRequest(httpclient,httppost);
        // 5. Obtain the authToken from the response
        String authToken = getAuthTokenFromResponse(response);

        LOGGER.debug( String.format("Authenticated installationId = [%s]",installationId));

        return authToken;

    }



    /**
     * Gets the Manifest from the .oci.yml file of a specific commit of a GitHub repository
     * @param path .oci.yml file path
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     *
     * @return oci
     */
    private RepositoryConfiguration getRepositoryConfiguration(String path) throws JsonMappingException, JsonProcessingException, IOException {
       return yamlMapper.readValue(getGitHubData(path), RepositoryConfiguration.class);
    }

    /**
     * Gets the Manifest from the manifest.json file of a specific commit of a GitHub repository
     * @param path manifest.json file path
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     *
     * @return manifest
     */
    private Manifest getManifest(String path)
            throws JsonMappingException, JsonProcessingException, IOException {
        String data = getGitHubData(path);
        return new Manifest(Arrays.asList(jsonMapper.readValue(data, ManifestEntry[].class)));
    }


    /**
     * Gets a collection of test cases from manifest of a specific commit of a GitHub repository.
     * For each manifest entry, gets the proper data needed for the creation of a new TestCase
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param commit                of the repository
     * @param ontologyFolder        repository folder that contains the ontology
     * @param testFolder            repository folder that contains the tests
     * @param mainifest             manifest of the repository
     *
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     *
     * @return test cases
     */
    private Collection<HubTestCase> getTestCasesFromManifest(String owner, String repo, String commit, String ontologyFolder, String testFolder, Manifest mainifest) throws IOException {
        Collection<HubTestCase> testCases = new ArrayList<HubTestCase>();
        String genericOntologyPath = getRawPath(owner, repo, commit)+ontologyFolder+SLASH;
        String genericTestPath = getRawPath(owner, repo, commit)+testFolder+SLASH;

        for(ManifestEntry entry:mainifest.getManifestEntries()){
            String name = entry.getName();
            String ontology = getGitHubData(genericOntologyPath+entry.getOntology());
            String instances = getGitHubData(genericTestPath+entry.getInstances());
            String schema = getGitHubData(genericTestPath+entry.getSchema());
            String producedSM = getGitHubData(genericTestPath+entry.getProducedShapeMap());
            String expectedSM = getGitHubData(genericTestPath+entry.getExpectedShapeMap());

            testCases.add(new HubTestCase(name,ontology,instances,schema,producedSM,expectedSM));
        }
        
        return testCases;
    }

    /**
     * Performs a request to a specific url and returns the content data of the file returned by the request
     * @param path  url
     * @return contend data
     */
    private String getGitHubData(String path) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = getGitHubGet(path);
        return executeRequest(httpclient,httpget);
    }


    /**
     * Gets the checkrunId value from the request result
     *
     * @param result of the response
     * @return checkrunId
     */
    private String getCheckRunIdFromResponse(String result){
        Map<String,Object> checkResponse = null;
        try {
            checkResponse = this.jsonMapper.readValue(result, Map.class);
            return String.valueOf(checkResponse.get("id"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds to the request the needed params to create a GitHub checkrun
     *
     * @param httppost
     * @param commit sha of the commit
     *
     * @return request with the params
     */
    private HttpPost addCreateCheckRunParams(HttpPost httppost, String commit){
        try {
            StringEntity params =  new StringEntity("{\"name\":\"ontolo-ci\",\"head_sha\":\""+commit+"\"}");
            httppost.setEntity(params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httppost;
    }

    /**
     * Adds to the request the needed params to update a GitHub checkrun
     *
     * @param conclusion    new status of the checkrun
     * @param output        output message
     *
     * @return request with the params
     */
    private HttpPatch addUpdateCheckRunParams(HttpPatch httppatch,String conclusion, String output)  {
        try {
            StringEntity params = new StringEntity("{\"conclusion\":\""+conclusion+"\",\"output\":"+output+"}");
            httppatch.setEntity(params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httppatch;
    }



    /**
     * Performs a request and returns the result as a String.
     *
     * @param httpclient
     * @param request
     * @return result
     */
    private String executeRequest( HttpClient httpclient,HttpRequestBase request) throws IOException {
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
                String result =  IOUtils.toString(instream, "UTF-8");
                if(result.startsWith("404:"))
                    throw new FileNotFoundException();
                if(result.replace("\n","").replace("\r","").length()<=0)
                    throw new EmptyContentFileException();
                return result;
            }
        }
        return null;
    }

    /**
     * Gets the installationId value from the request result
     *
     * @param result    of the request
     * @param owner     of the installation
     *
     * @return installationId
     */
    private String getInstallationIdFromResponse(String result,String owner) {
        Map<String,Object> installation = null;
        try {
            installation = this.jsonMapper.readValue(result, Map.class);
            return String.valueOf(installation.get("id"));
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * Gets the authorization token value from the request result
     *
     * @param  result   of the request
     * @return authorization token
     */
    private String getAuthTokenFromResponse(String result) {
        Map<String,Object> content = null;
        try {
            content = this.jsonMapper.readValue(result, Map.class);
            return (String) content.get("token");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Creates a new specific GET request with the common GitHub headers
     *
     * @param path  for the request
     * @return GET request
     */
    private HttpGet getGitHubGet(String path){
        HttpGet httpGet = new HttpGet(path);
        httpGet = (HttpGet) setGitHubHeaders(httpGet);
        return httpGet;
    }

    /***
     * Creates a new specific GET request with the common GitHub headers and Bearer Authorization Header
     *
     * @param path  for the request
     * @return GET request
     */
    private HttpGet getGitHubGetAuth(String path){
        HttpGet httpGet = getGitHubGet(path);
        String jwt = "Bearer "+KeyUtils.getJWT();
        LOGGER.debug("jwt"+jwt);
        httpGet.setHeader("Authorization", jwt);
        return httpGet;
    }

    private HttpGet getGitHubPostAuth(String path){
        HttpGet httpGet = getGitHubGet(path);
        String jwt = "Bearer "+KeyUtils.getJWT();
        httpGet.setHeader("Authorization", jwt);
        return httpGet;
    }

    /***
     * Creates a new specific POST request with the common GitHub headers
     *
     * @param path  for the request
     * @return POST request
     */
    private HttpPost getGitHubPost(String path){
        HttpPost httppost = new HttpPost(path);
        httppost = (HttpPost) setGitHubHeaders(httppost);
        return httppost;
    }

    /***
     * Creates a new specific POST request with the common GitHub headers and Authorization Header
     *
     * @param path  for the request
     * @return POST request
     */
    private HttpPost getGitHubPostAuth(String path,String authToken){
        HttpPost httppost = getGitHubPost(path);
        httppost.addHeader("Authorization", "token "+authToken);
        return httppost;
    }

    /***
     * Creates a new specific POST request with the common GitHub headers and Bearer Authorization Header
     *
     * @param path  for the request
     * @return POST request
     */
    private HttpPost getBearerGitHubPost(String path) {
        HttpPost httppost = getGitHubPost(path);
        httppost.addHeader("Authorization", "Bearer "+KeyUtils.getJWT());
        return httppost;
    }


    /***
     * Creates a new specific PATCH request with the common GitHub headers and Authorization Header
     *
     * @param path  for the request
     * @return POST request
     */
    private HttpPatch getGitHubPatchAuth(String path,String authToken){
        HttpPatch httpatch = new HttpPatch(path);
        httpatch = (HttpPatch) setGitHubHeaders(httpatch);
        httpatch.addHeader("Authorization", "token "+authToken);
        return httpatch;
    }

    /**
     * Sets the common GitHub headers to a specific request
     *
     * @param   requestBase
     * @return  request
     */
    private HttpRequestBase setGitHubHeaders(HttpRequestBase requestBase){
        requestBase.addHeader("Accept", "application/vnd.github.v3+json");
        requestBase.addHeader("content-type", "application/json");
        return requestBase;
    }



    /**
     * Returns the full path for the GitHub Raw requests
     *
     * @param owner   of the repository
     * @param repo    the repository name
     * @param commit  of the repository
     *
     * @return path as a string
     */
    private String getRawPath(final String owner, final String repo, final String commit) {
        return GITHUB_RAW_REQUEST +owner+SLASH+repo+SLASH+commit+SLASH;
    }

    /**
     * Returns the full path for the GitHub API Check Runs requests
     *
     * @param owner   of the repository
     * @param repo    the repository name
     *
     * @return path as a string
     */
    private String getCheckRunsPath(final String owner, final String repo) {
        return GITHUB_API_REQUEST+"repos"+SLASH+owner+SLASH+repo+SLASH+"check-runs";
    }

    /**
     * Returns the full path to update a Check Run via GitHub API
     *
     * @param owner         of the repository
     * @param repo          the repository name
     * @param checkRunId
     *
     * @return path as a string
     */
    private String getUpdateCheckRunPath(final String owner, final String repo, final String checkRunId) {
        return getCheckRunsPath(owner,repo)+SLASH+checkRunId;
    }

    /**
     * Returns the full path to obtain the YAML_FILE_NAME of a concrete commit of a concrete repository via GitHub Raw
     *
     * @param owner   of the repository
     * @param repo    the repository name
     * @param commit  of the repository
     *
     * @return path as a string
     */
    private String getYAMLPath(final String owner, final String repo, final String commit){
        return getRawPath(owner,repo,commit) + YAML_FILE_NAME;
    }

    /**
     * Returns the full path to obtain the manifest of a concrete commit of a concrete repository via GitHub Raw
     *
     * @param owner               of the repository
     * @param repo                the repository name
     * @param commit              of the repository
     * @param repositoryConfig    the configuration of the repostitory
     *
     * @return path as a string
     */
    private String getManifestPath(final String owner, final String repo, final String commit,final RepositoryConfiguration repositoryConfig){
        return getRawPath(owner, repo, commit) + repositoryConfig.getManifestPath();
    }

    /**
     * Returns the full path to authenticate by installation via GitHub API
     *
     * @param installationId
     * @return path as a string
     */
    private String getAuthenticationByInstallationPath(String installationId) {
        return GITHUB_API_REQUEST+"app/installations/"+installationId+"/access_tokens";
    }






}
