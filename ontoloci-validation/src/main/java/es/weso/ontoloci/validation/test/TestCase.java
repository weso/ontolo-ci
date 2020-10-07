package es.weso.ontoloci.validation.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This models a test case, which is composed of a name, the ontology that is
 * being tested, the data to test, the schema against the tests will be thrown,
 * the input shape map, which specifies the nodes that will be tested. And the
 * output expected shape map.
 *
 * @author Pablo Menéndez
 */
public class TestCase {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCase.class);

    private final String name;
    private final String ontology;
    private final String instances;
    private final String schema;
    private final String expectedShapeMap;
    private final String producedShapeMap;

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
    public TestCase(final String name, final String ontology, final String instances,
                    final String schema, final String expectedShapeMap,
                    final String producedShapeMap) {
        this.name = name;
        this.ontology = ontology;
        this.instances = instances;
        this.schema = schema;
        this.expectedShapeMap = expectedShapeMap;
        this.producedShapeMap = producedShapeMap;

        LOGGER.debug("Creating a test case " + this.toString());
    }

    /**
     * Gets name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets ontology file path.
     *
     * @return ontology file path
     */
    public String getOntology() {
        return ontology;
    }

    /**
     * Gets data file path.
     *
     * @return data file path
     */
    public String getInstances() {
        return instances;
    }

    /**
     * Gets test schema file path.
     *
     * @return test schema file path
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Gets test shape map file path.
     *
     * @return test shape map file path
     */
    public String getExpectedShapeMap() {
        return expectedShapeMap;
    }

    /**
     * Gets expected shape map file path.
     *
     * @return the expected shape map file path
     */
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
