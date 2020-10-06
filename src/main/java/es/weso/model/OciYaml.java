package es.weso.model;


public class OciYaml {


	private String manifest;

	public OciYaml() {
	}
	
	public OciYaml(String manifest) {
		this.manifest = manifest;
	}

	public String getManifest() {
		return manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}

	@Override
	public String toString() {
		return "OciYaml [manifest=" + manifest + "]";
	}
	
	

}
