package es.weso.ontoloci.worker.test;

import es.weso.ontoloci.persistence.PersistedTestCase;
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
    private final String producedShapeMap;
    private final String expectedShapeMap;


    /**
     * The default constructor is a basic all-args constructor. All the arguments
     * that it takes are the contents of the different files, except the name that is the
     * literal string that will be assigned as test name.
     *
     * @param name                     of the test case.
     * @param ontology         is the content of the ontology file used
     *                                 for testing.
     * @param instances             is the content of the data file used to
     *                                 mock ontology instances.
     * @param schema       is the content of the schema used to
     *                                 validate the ontology.
     * @param expectedShapeMap     is the content of the shape map that
     *                                 relates each test data node with its
     *                                 corresponding schema.
     * @param producedShapeMap is the content of the expected result shape
     *                                 map.
     */
    public TestCase(final String name, final String ontology, final String instances,
                    final String schema, final String producedShapeMap,
                    final String expectedShapeMap) {
        this.name = name;
        this.ontology = ontology;
        this.instances = instances;
        this.schema = schema;
        this.producedShapeMap = producedShapeMap;
        this.expectedShapeMap = expectedShapeMap;

        LOGGER.debug("Creating a test case ");
    }

    public static PersistedTestCase toPersistedTestCase(TestCase testCase) {
        return new PersistedTestCase(
                testCase.name,
                testCase.ontology,
                testCase.instances,
                testCase.schema,
                testCase.producedShapeMap,
                testCase.expectedShapeMap
        );
    }

    public static TestCase from(PersistedTestCase persistedTestCase) {
        return new TestCase(
                persistedTestCase.getName(),
                persistedTestCase.getOntology(),
                persistedTestCase.getInstances(),
                persistedTestCase.getSchema(),
                persistedTestCase.getProducedShapeMap(),
                persistedTestCase.getExpectedShapeMap()
        );
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
     * Gets data content.
     *
     * @return data content
     */
    public String getInstances() {
        return instances;
    }

    /**
     * Gets test schema content.
     *
     * @return test schema content
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Gets test shape map content.
     *
     * @return test shape map content
     */
    public String getExpectedShapeMap() {
        return expectedShapeMap;
    }

    /**
     * Gets expected shape map content.
     *
     * @return the expected shape map content
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
