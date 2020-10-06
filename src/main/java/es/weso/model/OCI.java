package es.weso.model;

public class OCI {

	private String manifestPath;

	public OCI() {
	}

	public OCI(String manifestPath) {
		this.manifestPath = manifestPath;
	}

	public String getManifestPath() {
		return manifestPath;
	}

	public void setManifestPaht(String manifestPath) {
		this.manifestPath = manifestPath;
	}

	@Override
	public String toString() {
		return "OciYaml [manifestPath=" + manifestPath + "]";
	}

}
