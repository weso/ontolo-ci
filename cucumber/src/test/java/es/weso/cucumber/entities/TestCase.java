package es.weso.cucumber.entities;

public class TestCase {

    private String name;
    private String ontology;
    private String instances;
    private String schema;
    private String producedShapeMap;
    private String expectedShapeMap;


    public TestCase(){}

    public String getName() {
        return name;
    }

    public String getOntology() {
        return ontology;
    }

    public String getInstances() {
        return instances;
    }

    public String getSchema() {
        return schema;
    }

    public String getExpectedShapeMap() {
        return expectedShapeMap;
    }

    public String getProducedShapeMap() {
        return producedShapeMap;
    }

    @Override
    public String toString() {
        return "TestCase [test_name=" + name + ", ontology=" + ontology + ", data=" + instances + ", schema="
                + schema + ", in_shape_map=" + expectedShapeMap + ", out_shape_map="
                + producedShapeMap + "]";
    }
}
