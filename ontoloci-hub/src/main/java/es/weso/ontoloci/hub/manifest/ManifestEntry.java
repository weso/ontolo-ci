package es.weso.ontoloci.hub.manifest;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This models a manifest entry, which is composed of a name and the file paths
 * of the ontology that is being tested, the data to test, the schema against
 * the tests will be thrown, the input shape map, which specifies the nodes that
 * will be tested. And the output expected shape map.
 *
 * @author Pablo Menéndez
 */
public class ManifestEntry {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestEntry.class);

    @JsonProperty("test_name")
    private String name;
    @JsonProperty("ontology")
    private String ontology;
    @JsonProperty("data")
    private String instances;
    @JsonProperty("schema")
    private String schema;
    @JsonProperty("in_shape_map")
    private String producedShapeMap;
    @JsonProperty("out_shape_map")
    private String expectedShapeMap;

    /**
     * Public empty constructor needed for the json mapper
     */
    public ManifestEntry(){}

    /**
     * The default constructor is a basic all-args constructor. All the arguments
     * that it takes are paths to the different files, except the name that is the
     * literal string that will be assigned as test name.
     *
     * @param name                     of the test case.
     * @param ontology         is the file path to the ontology file used
     *                                 for testing.
     * @param instances             is the file path to the data file used to
     *                                 mock ontology instances.
     * @param schema       is the file path to the schema used to
     *                                 validate the ontology.
     * @param expectedShapeMap     is the file path to the shape map that
     *                                 relates each test data node with its
     *                                 corresponding schema.
     * @param producedShapeMap is the file path to the expected result shape
     *                                 map.
     */
    public ManifestEntry(final String name, final String ontology, final String instances,
                    final String schema, final String producedShapeMap,
                    final String expectedShapeMap) {
        this.name = name;
        this.ontology = ontology;
        this.instances = instances;
        this.schema = schema;
        this.producedShapeMap = producedShapeMap;
        this.expectedShapeMap = expectedShapeMap;

        LOGGER.debug(String.format("Creating new ManifestEntry from the public constructor with name=[%s]", this.name));
    }

    /**
     * Gets name.
     * @return name
     */
    public String getName() {
        LOGGER.debug(String.format("GET reading the manifest entry name of a ManifestEntry, returning [%s]", this.name));
        return name;
    }

    /**
     * Gets ontology file path.
     *
     * @return ontology file path
     */
    public String getOntology() {
        LOGGER.debug(String.format("GET reading the ontology of the Manifest, returning an ontology with length [%s]", this.ontology.length()));
        return ontology;
    }

    /**
     * Gets data file path.
     *
     * @return data file path
     */
    public String getInstances() {
        LOGGER.debug(String.format("GET reading the instances of the Manifest, returning an instance with length [%s]", this.instances.length()));
        return instances;
    }

    /**
     * Gets test schema file path.
     *
     * @return test schema file path
     */
    public String getSchema() {
        LOGGER.debug(String.format("GET reading the schema of the Manifest, returning an schema with length [%s]", this.schema.length()));
        return schema;
    }

    /**
     * Gets test shape map file path.
     *
     * @return test shape map file path
     */
    public String getExpectedShapeMap() {
        LOGGER.debug(String.format("GET reading the expected Shape Map of the Manifest, returning a shape map with length [%s]", this.expectedShapeMap.length()));
        return expectedShapeMap;
    }

    /**
     * Gets expected shape map file path.
     *
     * @return the expected shape map file path
     */
    public String getProducedShapeMap() {
        LOGGER.debug(String.format("GET reading the produced shape map of the Manifest, returning a shape map with length [%s]", this.producedShapeMap.length()));
        return producedShapeMap;
    }

    @Override
    public String toString() {
        return "TestCase [test_name=" + name + ", ontology=" + ontology + ", data=" + instances + ", schema="
                + schema + ", in_shape_map=" + expectedShapeMap + ", out_shape_map="
                + producedShapeMap + "]";
    }

}
