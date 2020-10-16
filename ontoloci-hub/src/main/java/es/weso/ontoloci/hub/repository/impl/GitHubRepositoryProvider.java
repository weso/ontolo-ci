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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class implements the needed methods to get a collection of TestCases
 * from a concrete branch and commit of a GitHub repository.
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

    public static GitHubRepositoryProvider empty() {
        return new GitHubRepositoryProvider();
    }

    public static GitHubRepositoryProvider with(ObjectMapper yamlMapper, ObjectMapper jsonMapper) {
        return new GitHubRepositoryProvider(yamlMapper, jsonMapper);
    }

    private GitHubRepositoryProvider() {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.jsonMapper = new ObjectMapper(new JsonFactory());
    }

    private GitHubRepositoryProvider(ObjectMapper yamlMapper, ObjectMapper jsonMapper) {
        this.yamlMapper = yamlMapper;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Gets a collection of test cases from a concrete branch and commit of a GitHub repository.
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param branch                of the repository
     *
     * @return test cases
     */
    @Override
    public Collection<HubTestCase> getTestCases(String owner, String repo, String branch) {
        RepositoryConfiguration repositoryConfig;
        Manifest manifest;
        String ontologyFolder;
        String testFolder;
        try {
            repositoryConfig = getRepositoryConfiguration(getConcatenatedPath(owner,repo,branch)+YAML_FILE_NAME);
            manifest = getManifest(getConcatenatedPath(owner,repo,branch) + repositoryConfig.getManifestPath());
            ontologyFolder = repositoryConfig.getOntologyFolder();
            testFolder = repositoryConfig.getTestFolder();
            return getTestCasesFromManifest(owner,repo,branch,ontologyFolder,testFolder,manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the Manifest from the .oci.yml file of a concrete branch and commit of a GitHub repository
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
     * Gets the Manifest from the manifest.json file of a concrete branch and commit of a GitHub repository
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
     * Gets a collection of test cases from manifest of a concrete branch and commit of a GitHub repository.
     * For each manifest entry, gets the proper data needed for the creation of a new TestCase
     *
     * @param owner                 of the repository
     * @param repo                  name of the repository
     * @param branch                of the repository
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
    private Collection<HubTestCase> getTestCasesFromManifest(String owner, String repo, String branch, String ontologyFolder, String testFolder, Manifest mainifest)
            throws JsonMappingException, JsonProcessingException, IOException {
        Collection<HubTestCase> testCases = new ArrayList<HubTestCase>();
        String genericOntologyPath = getConcatenatedPath(owner, repo, branch)+ontologyFolder+SLASH_SYMBOL;
        String genericTestPath = getConcatenatedPath(owner, repo, branch)+testFolder+SLASH_SYMBOL;
        
        
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
    private String getData(String path) {
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
        }

        return null;
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

    private String getConcatenatedPath(String owner,String repo, String branch) {
        return API_REQUEST+owner+SLASH_SYMBOL+repo+SLASH_SYMBOL+branch+SLASH_SYMBOL;
    }

}
