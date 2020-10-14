package es.weso.ontoloci.hub.oci;

public class OCI {
    private String manifestPath;

    public OCI() {} // Needed for the YAML mapper

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
