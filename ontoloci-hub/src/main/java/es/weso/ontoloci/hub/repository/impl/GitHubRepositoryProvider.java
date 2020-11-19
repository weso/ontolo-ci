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
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
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
 * This class implements the needed methods to get a collection of TestCases
 * from a concrete commit of a GitHub repository.
 *
 * @author Pablo Menéndez
 */
public class GitHubRepositoryProvider implements RepositoryProvider {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRepositoryProvider.class);

    private final static String API_REQUEST = "https://raw.githubusercontent.com/";
    private final static String YAML_FILE_NAME = ".oci.yml";
    private final static String SLASH_SYMBOL = "/";

    private final ObjectMapper yamlMapper;
    private final ObjectMapper jsonMapper;

    /**
     * Creates an empty default github repository provider object.
     * It initializes both YAML and JSON mappers to the default values.
     *
     * @return a new GitHubRepositoryProvider object with default configurations.
     */
    public static GitHubRepositoryProvider empty() {

        LOGGER.debug(
                String.format(
                        "NEW Creating new GitHubRepositoryProvider from the static factory with default YAML and JSON mappers"
                )
        );

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

        LOGGER.debug(
                String.format(
                        "NEW Creating new GitHubRepositoryProvider from the static factory with a custom YAML and JSON mappers"
                )
        );

        return new GitHubRepositoryProvider(yamlMapper, jsonMapper);
    }

    private GitHubRepositoryProvider() {

        LOGGER.debug(
                String.format(
                        "NEW Creating new GitHubRepositoryProvider from the public constructor with default YAML and JSON mappers"
                )
        );

        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.jsonMapper = new ObjectMapper(new JsonFactory());
    }

    private GitHubRepositoryProvider(final ObjectMapper yamlMapper, final ObjectMapper jsonMapper) {

        LOGGER.debug(
                String.format(
                        "NEW Creating new GitHubRepositoryProvider from the public constructor with a custom YAML and JSON mappers"
                )
        );

        this.yamlMapper = yamlMapper;
        this.jsonMapper = jsonMapper;
    }


    public String getPersonalAccessToken(String code){
        String access_token = "";
        String clientId = KeyUtils.getClientId();
        String clientSecret = KeyUtils.getClientSecret();

        System.out.println(clientId);
        System.out.println(clientSecret);

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://github.com/login/oauth/access_token");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("code", code));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                    String content = IOUtils.toString(instream, "UTF-8");
                    access_token = content.split("access_token=")[1].split("&expires_in=")[0];
                }
            }

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_token;
    }


    public String getInstallationId(String owner){
        URL url;
        HttpURLConnection con;
        try {
            url = new URL("https://api.github.com/app/installations");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "application/vnd.github.v3+json");
            con.setRequestProperty("Authorization", "Bearer "+KeyUtils.getJWT());

            String content =  getFileContent(con.getInputStream());
            List<Map<String,Object>> installations = this.jsonMapper.readValue(content,List.class);
            for(Map<String,Object> installation: installations) {
                String accountData = (String) ((Map<String, Object>) installation.get("account")).get("login");
                if (accountData.equals(owner))
                    return String.valueOf(installation.get("id"));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String authenticateByInstallation(String installationId)  {
        String jwt = KeyUtils.getJWT();
        URL url;
        HttpURLConnection con;
        try {
            url = new URL("https://api.github.com/app/installations/"+installationId+"/access_tokens");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "application/vnd.github.v3+json");
            con.setRequestProperty("Authorization", "Bearer "+jwt);

            String content =  getFileContent(con.getInputStream());
            Map<String,Object> response = this.jsonMapper.readValue(content,Map.class);
            return (String) response.get("token");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createCheckRun(String authToken,String owner,String repo,String headSha){
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://api.github.com/repos/"+owner+"/"+repo+"/check-runs");

        httppost.addHeader("Accept", "application/vnd.github.v3+json");
        httppost.addHeader("Authorization", "token "+authToken);
        httppost.addHeader("content-type", "application/json");

        try {
            // Request parameters and other properties.
            StringEntity params = new StringEntity("{\"name\":\"ontolo-ci\",\"head_sha\":\""+headSha+"\"}");
            httppost.setEntity(params);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();


            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                    String content = IOUtils.toString(instream, "UTF-8");
                    Map<String,Object> checkResponse = this.jsonMapper.readValue(content,Map.class);
                    return String.valueOf(checkResponse.get("id"));
                }
            }

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void updateCheckRun(String authToken, String checkRunId, String owner, String repo, String conclusion,String output) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPatch httpatch = new HttpPatch("https://api.github.com/repos/"+owner+"/"+repo+"/check-runs/"+checkRunId);

        httpatch.addHeader("Accept", "application/vnd.github.v3+json");
        httpatch.addHeader("Authorization", "token "+authToken);
        httpatch.addHeader("content-type", "application/json");

        try {
            // Request parameters and other properties.
            StringEntity params = new StringEntity("{\"conclusion\":\""+conclusion+"\",\"output\":"+output+"}");
            httpatch.setEntity(params);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httpatch);
            HttpEntity entity = response.getEntity();


            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                    String content = IOUtils.toString(instream, "UTF-8");
                    System.out.println(content);

                }
            }

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        LOGGER.debug(
                String.format(
                        "GET Computing the collection of HubTestCase for user=[%s], repo =[%s] and commit=[%s]",
                        owner,
                        repo,
                        commit
                )
        );

        // Result collection, initialized to empty one so not null is returned.
        final Collection<HubTestCase> hubTestCases = new ArrayList<>();


        // Get the repository configuration file.
        final RepositoryConfiguration repositoryConfig =
                getRepositoryConfiguration(getConcatenatedPath(owner,repo,commit) + YAML_FILE_NAME);

        // Parse the repository configuration file and create a manifest object
        final Manifest manifest =
                getManifest(getConcatenatedPath(owner,repo,commit) + repositoryConfig.getManifestPath());

        // Get the ontology folder
        final String ontologyFolder = repositoryConfig.getOntologyFolder();

        // Get the tests folder
        final String testsFolder = repositoryConfig.getTestFolder();

        // Get collection of generated test cases from the manifest file.
        final Collection<HubTestCase> parsedTestCases = getTestCasesFromManifest(owner,repo,commit,ontologyFolder,testsFolder,manifest);

        LOGGER.debug(
                String.format(
                "INTERNAL parsed test cases [%s]",
                parsedTestCases.size())
        );

        // Add all the test cases to the result collection.
        hubTestCases.addAll(parsedTestCases);


        return hubTestCases;
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
        String genericOntologyPath = getConcatenatedPath(owner, repo, commit)+ontologyFolder+SLASH_SYMBOL;
        String genericTestPath = getConcatenatedPath(owner, repo, commit)+testFolder+SLASH_SYMBOL;
        
        
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

            return getFileContent(con.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    private String getFileContent(InputStream in) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line+'\n');
        }
        rd.close();
        return result.toString();
    }

    private String getConcatenatedPath(final String owner,final String repo,final String commit) {
        return API_REQUEST+owner+SLASH_SYMBOL+repo+SLASH_SYMBOL+commit+SLASH_SYMBOL;
    }


}
