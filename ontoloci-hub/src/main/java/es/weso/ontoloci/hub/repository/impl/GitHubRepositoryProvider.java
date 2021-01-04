package es.weso.ontoloci.hub.repository.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
 * TODO
 *
 * @author Pablo Menéndez
 */
public class GitHubRepositoryProvider implements RepositoryProvider {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRepositoryProvider.class);

    private final static String GITHUB_RAW_REQUEST      =   "https://raw.githubusercontent.com/";
    private final static String GITHUB_API_REQUEST      =   "https://api.github.com/";
    private final static String PERSONAL_TOKEN_REQUEST  =   "https://github.com/login/oauth/access_token";
    private final static String INSTALLATION_REQUEST    =   "https://api.github.com/app/installations";

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
     * Gets a collection of test cases from a concrete commit of a GitHub repository.
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

        // Get the repository configuration file.
        final RepositoryConfiguration repositoryConfig = getRepositoryConfiguration(getYAMLPath(owner,repo,commit));

        // Parse the repository configuration file and create a manifest object
        final Manifest manifest = getManifest(getManifestPath(owner,repo,commit,repositoryConfig));

        // Get the ontology folder
        final String ontologyFolder = repositoryConfig.getOntologyFolder();

        // Get the tests folder
        final String testsFolder = repositoryConfig.getTestFolder();

        // Get collection of generated test cases from the manifest file.
        final Collection<HubTestCase> parsedTestCases = getTestCasesFromManifest(owner,repo,commit,ontologyFolder,testsFolder,manifest);

        LOGGER.debug(String.format("INTERNAL parsed test cases [%s]",parsedTestCases.size()));

        // Add all the test cases to the result collection.
        hubTestCases.addAll(parsedTestCases);

        return hubTestCases;
    }


    @Override
    public String createCheckRun(String authToken,String owner,String repo,String commit){

        LOGGER.debug( String.format("Creating CheckRun  for user=[%s], repo =[%s] and commit =[%s] ",owner,repo,commit));

        HttpClient httpclient = HttpClients.createDefault();
        String path = getCheckRunsPath(owner,repo);
        HttpPost httppost = getGitHubPostAuth(path,authToken);
        String response = executeRequest(httpclient,httppost,getCreateCheckRunParams(commit));
        String result =  getCheckRunIdFromResponse(response);

        LOGGER.debug( String.format("Created CheckRun for user=[%s], repo =[%s] and commit =[%s] ",owner,repo,commit));

        return result;
    }


    @Override
    public void updateCheckRun(String authToken, String checkRunId, String owner, String repo, String conclusion,String output) {

        LOGGER.debug( String.format("Updating CheckRun = [%s] for user=[%s] and repo =[%s] ",checkRunId,owner,repo));

        HttpClient httpclient = HttpClients.createDefault();
        String path = getUpdateCheckRunPath(owner,repo,checkRunId);
        HttpPatch httpatch = getGitHubPatchAuth(path,authToken);
        executeRequest(httpclient,httpatch,getUpdateCheckRunParams(conclusion,output));

        LOGGER.debug( String.format("CheckRun updated = [%s] for user=[%s] and repo =[%s] ",checkRunId,owner,repo));
    }



    public String getPersonalAccessToken(String code){

        LOGGER.debug("Getting the Personal Access token");

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = getGitHubPost(PERSONAL_TOKEN_REQUEST);

        StringEntity params = getPersonalAccessTokenParams(code);
        String response = executeRequest(httpclient,httppost,params);
        String access_token = getAccessTokenFromResponse(response);

        LOGGER.debug("Personal Access token obtained");

        return access_token;
    }


    public String getInstallationId(String owner) {

        LOGGER.debug( String.format("Getting InstallationId for user=[%s] ",owner));

        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = getGitHubGetAuth(INSTALLATION_REQUEST);
        String response = executeRequest(httpclient,httpget,null);
        String id = getInstallationIdFromResponse(response,owner);

        LOGGER.debug( String.format("InstallationId obtained for user=[%s] ",owner));

        return id;
    }


    public String authenticateByInstallation(String installationId)  {

        LOGGER.debug( String.format("Authenticating installationId = [%s]",installationId));

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = getBearerGitHubPost(getAuthenticationByInstallationPath(installationId));
        String response = executeRequest(httpclient,httppost,null);
        String authToken = getAuthTokenFromResponse(response);

        LOGGER.debug( String.format("Authenticated installationId = [%s]",installationId));

        return authToken;

    }




    /**
     * Gets the Manifest from the .oci.yml file of a concrete commit of a GitHub repository
     * @param path .oci.yml file path
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     *
     * @return oci
     */
    private RepositoryConfiguration getRepositoryConfiguration(String path) throws JsonMappingException, JsonProcessingException, IOException {
       return yamlMapper.readValue(getData(path), RepositoryConfiguration.class);
    }

    /**
     * Gets the Manifest from the manifest.json file of a concrete commit of a GitHub repository
     * @param path manifest.json file path
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     *
     * @return manifest
     */
    private Manifest getManifest(String path)
            throws JsonMappingException, JsonProcessingException, IOException {
        return new Manifest(Arrays.asList(jsonMapper.readValue(getData(path), ManifestEntry[].class)));
    }


    /**
     * Gets a collection of test cases from manifest of a concrete commit of a GitHub repository.
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
            String ontology = getData(genericOntologyPath+entry.getOntology());
            String instances = getData(genericTestPath+entry.getInstances());
            String schema = getData(genericTestPath+entry.getSchema());
            String producedSM = getData(genericTestPath+entry.getProducedShapeMap());
            String expectedSM = getData(genericTestPath+entry.getExpectedShapeMap());

            testCases.add(new HubTestCase(name,ontology,instances,schema,producedSM,expectedSM));
        }
        
        return testCases;
    }

    /**
     * Performs a request to a concrete url and returns the content data of the file returned by the request
     * @param path  url
     * @return contend data
     */
    private String getData(String path) throws IOException {
        URL url;
        HttpURLConnection con;
        try {
            url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "application/json");

            return getStreamContent(con.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Gets the content of a stream
     * @param  in stream
     * @return the content of the stream
     * @throws IOException
     */
    private String getStreamContent(InputStream in) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line+'\n');
        }
        rd.close();
        return result.toString();
    }






    private String getCheckRunIdFromResponse(String response){
        Map<String,Object> checkResponse = null;
        try {
            checkResponse = this.jsonMapper.readValue(response, Map.class);
            return String.valueOf(checkResponse.get("id"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringEntity getCreateCheckRunParams(String commit){
        try {
            return new StringEntity("{\"name\":\"ontolo-ci\",\"head_sha\":\""+commit+"\"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringEntity getUpdateCheckRunParams(String conclusion, String output)  {
        try {
            return new StringEntity("{\"conclusion\":\""+conclusion+"\",\"output\":"+output+"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private StringEntity getPersonalAccessTokenParams(String code){
        String clientId = KeyUtils.getClientId();
        String clientSecret = KeyUtils.getClientSecret();

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);

        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("code", code));

        try {
            return new UrlEncodedFormEntity(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    private String executeRequest( HttpClient httpclient,HttpRequestBase request,StringEntity params) {

        try {

            if(request instanceof HttpEntityEnclosingRequestBase)
                if(params!=null)
                    ((HttpEntityEnclosingRequestBase) request).setEntity(params);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    return IOUtils.toString(instream, "UTF-8");
                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private String getInstallationIdFromResponse(String response,String owner) {
        List<Map<String,Object>> installations = null;
        try {
            installations = this.jsonMapper.readValue(response, List.class);
            for (Map<String, Object> installation : installations) {
                String accountData = (String) ((Map<String, Object>) installation.get("account")).get("login");
                if (accountData.equals(owner))
                    return String.valueOf(installation.get("id"));

            }
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  null;
    }


    private String getAuthTokenFromResponse(String response) {
        Map<String,Object> content = null;
        try {
            content = this.jsonMapper.readValue(response, Map.class);
            return (String) content.get("token");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



    private HttpGet getGitHubGetAuth(String path){
        HttpGet httpGet = new HttpGet(path);
        httpGet = (HttpGet) setGitHubHeaders(httpGet);
        httpGet.setHeader("Authorization", "Bearer "+KeyUtils.getJWT());
        return httpGet;
    }

    private HttpPost getGitHubPost(String path){
        HttpPost httppost = new HttpPost(path);
        httppost = (HttpPost) setGitHubHeaders(httppost);
        return httppost;
    }

    private HttpPost getGitHubPostAuth(String path,String authToken){
        HttpPost httppost = getGitHubPost(path);
        httppost.addHeader("Authorization", "token "+authToken);
        return httppost;
    }

    private HttpPost getBearerGitHubPost(String path) {
        HttpPost httppost = getGitHubPost(path);
        String jwt = KeyUtils.getJWT();
        httppost.addHeader("Authorization", "Bearer "+jwt);
        return httppost;
    }


    private HttpPatch getGitHubPatchAuth(String path,String authToken){
        HttpPatch httpatch = new HttpPatch(path);
        httpatch = (HttpPatch) setGitHubHeaders(httpatch);
        httpatch.addHeader("Authorization", "token "+authToken);
        return httpatch;
    }




    private HttpRequestBase setGitHubHeaders(HttpRequestBase requestBase){
        requestBase.addHeader("Accept", "application/vnd.github.v3+json");
        requestBase.addHeader("content-type", "application/json");
        return requestBase;
    }


    private String getAccessTokenFromResponse(String response) {
        return response.split("access_token=")[1].split("&expires_in=")[0];
    }


    /**
     * Returns the full path for the GitHub Raw requests
     * @param owner   of the repository
     * @param repo    the repository name
     * @param commit  of the repository
     *
     * @return path as a string
     */
    private String getRawPath(final String owner, final String repo, final String commit) {
        return GITHUB_RAW_REQUEST +owner+SLASH+repo+SLASH+commit+SLASH;
    }

    private String getCheckRunsPath(final String owner, final String repo) {
        return GITHUB_API_REQUEST+"repos"+SLASH+owner+SLASH+repo+SLASH+"check-runs";
    }

    private String getUpdateCheckRunPath(final String owner, final String repo, final String checkRunId) {
        return getCheckRunsPath(owner,repo)+SLASH+checkRunId;
    }

    private String getYAMLPath(final String owner, final String repo, final String commit){
        return getRawPath(owner,repo,commit) + YAML_FILE_NAME;
    }

    private String getManifestPath(final String owner, final String repo, final String commit,RepositoryConfiguration repositoryConfig){
        return getRawPath(owner, repo, commit) + repositoryConfig.getManifestPath();
    }

    private String getAuthenticationByInstallationPath(String installationId) {
        return GITHUB_API_REQUEST+"app/installations/"+installationId+"/access_tokens";
    }






}
