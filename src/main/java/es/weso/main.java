package es.weso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import es.weso.model.Manifest;
import es.weso.model.OciYaml;

/**
 * 
 * Sorry about this...
 *
 */

public class main {

	public static void main(String[] args) {
		
	
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ObjectMapper jsonmapper = new ObjectMapper(new JsonFactory());

		
		URL url;
		HttpURLConnection con;
		 StringBuilder result = new StringBuilder();
		try {
			url = new URL("https://raw.githubusercontent.com/mistermboy/oci-test/main/.oci.yml");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			con.setDoOutput(true);
			con.setRequestProperty("Accept", "application/json");
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			System.out.println(result.toString());
			
			
			
			OciYaml oci = mapper.readValue(result.toString(), OciYaml.class);
			
		
	        System.out.println(oci);
	        
	        url = new URL("https://raw.githubusercontent.com/mistermboy/oci-test/main/"+oci.getManifest());
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			con.setDoOutput(true);
			con.setRequestProperty("Accept", "application/json");
			
			rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			line = "";
			result = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			System.out.println(result.toString());
			
			Manifest[] manifest = mapper.readValue(result.toString(), Manifest[].class);
			
			for(Manifest m:manifest) {
				System.out.println(m.toString());
			}
			
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
