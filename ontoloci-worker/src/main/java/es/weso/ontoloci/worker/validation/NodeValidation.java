package es.weso.ontoloci.worker.validation;

public class NodeValidation {

    private String node;
    private String expected;
    private String provided;

    public NodeValidation(String node, String expected, String provided) {
        this.expected = expected;
        this.provided = provided;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getProvided() {
        return provided;
    }

    public void setProvided(String provided) {
        this.provided = provided;
    }
}
