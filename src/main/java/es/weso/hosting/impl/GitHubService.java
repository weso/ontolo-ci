package es.weso.hosting.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import es.weso.hosting.HostingService;
import es.weso.model.Manifest;
import es.weso.model.OCI;
import es.weso.model.manifestImpl.ManifestData;
import es.weso.model.manifestImpl.ManifestPath;

public class GitHubService implements HostingService {

	final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	final ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
	private final static String API_REQUEST = "https://raw.githubusercontent.com/";
	private final static String YAML_FILE_NAME = ".oci.yml";
	private final static String SLASH_SYMBOL = "/";

	@Override
	public Manifest[] getValidationData(String owner, String repo, String branch,String ontologyFolder,String testFolder) {
		OCI oci;
		ManifestPath[] manifestPaht;
		try {
			oci = getOCI(getConcatenatedPath(owner,repo,branch)+YAML_FILE_NAME);
			manifestPaht = getManifestPaths(getConcatenatedPath(owner,repo,branch) + oci.getManifestPath());
			return getManifestData(owner,repo,branch,ontologyFolder,testFolder,manifestPaht);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private OCI getOCI(String path) throws JsonMappingException, JsonProcessingException, IOException {
		return yamlMapper.readValue(getData(path), OCI.class);
	}

	private ManifestPath[] getManifestPaths(String path)
			throws JsonMappingException, JsonProcessingException, IOException {
		return jsonMapper.readValue(getData(path), ManifestPath[].class);
	}

	private ManifestData[] getManifestData(String owner,String repo,String branch,String ontologyFolder,String testFolder,ManifestPath[] manifestPaths)
			throws JsonMappingException, JsonProcessingException, IOException {
		ManifestData[] manifestDatas = new ManifestData[manifestPaths.length];
		String genericOntologyPath = getConcatenatedPath(owner, repo, branch)+ontologyFolder+SLASH_SYMBOL;
		
		String genericTestPath = getConcatenatedPath(owner, repo, branch)+testFolder+SLASH_SYMBOL;
		for (int i = 0; i < manifestPaths.length; i++) {
			ManifestData mD = new ManifestData();
			mD.setTest_name(manifestPaths[i].getTest_name());
			mD.setOntology(getData(genericOntologyPath+manifestPaths[i].getOntology()));
			mD.setData(getData(genericTestPath+manifestPaths[i].getData()));
			mD.setSchema(getData(genericTestPath+manifestPaths[i].getSchema()));
			mD.setIn_shape_map(getData(genericTestPath+manifestPaths[i].getIn_shape_map()));
			mD.setOut_shape_map(getData(genericTestPath+manifestPaths[i].getOut_shape_map()));
			manifestDatas[i] = mD;
		}

		return manifestDatas;
	}

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
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	private String getFileContent(InputStream in) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(in));
		StringBuilder result = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	private String getConcatenatedPath(String owner,String repo, String branch) {
		return API_REQUEST+owner+SLASH_SYMBOL+repo+SLASH_SYMBOL+branch+SLASH_SYMBOL;
	}

}
