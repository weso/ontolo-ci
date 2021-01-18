package es.weso.ontoloci.worker.build;

import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.test.HubTestCase;
import es.weso.ontoloci.worker.test.TestCase;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Build {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(Build.class);

    private String id;
    private Map<String, String> metadata;
    private Collection<TestCase> testCases;

    /**
     * Factory method that creates a build instance from an array of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static Build from(final TestCase... testCases) {
        LOGGER.debug("Factory method creating a new build for ");
        return new Build(Arrays.asList(testCases),new HashMap<>());
    }

    /**
     * Factory method that creates a build instance from a list of test cases.
     *
     * @param testCases from which to create the new build instance.
     * @return the new build instance.
     */
    public static Build from(final Collection<TestCase> testCases) {
        LOGGER.debug("Factory method creating a new build for " + testCases);
        return new Build(testCases,new HashMap<>());
    }

    /**
     * Private constructor. It creates a build instance from a collection of test cases.
     *
     * @param testCases from which to create the build.
     */
    private Build(final Collection<TestCase> testCases,Map<String, String> metadata) {
        this.id = UUID.randomUUID().toString();
        this.testCases = testCases;
        this.metadata = metadata;

        LOGGER.debug("Creating a new build for " + this);
    }

    /**
     * Gets an unmodifiable collection of the collection of test cases.
     *
     * @return an unmodifiable collection of the collection of test cases.
     */
    public Collection<TestCase> getTestCases() {
        return Collections.unmodifiableCollection(this.testCases);
    }

    /**
     * Sets the tests cases collection to the given one.
     *
     * @param testCases collection to be set.
     */
    public void setTestCases(final Collection<TestCase> testCases) {
        this.testCases = testCases;
    }

    /**
     * Gets the metadata map associated to the build.
     *
     * @return the metadata map.
     */
    public Map<String, String> getMetadata() {
        LOGGER.debug("Getting the metadata of the test case result " + this.metadata.toString()
                + " from " + this);

        return Collections.unmodifiableMap(this.metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }


    /**
     * Returns a HubBuild object clone
     * @return HubBuild
     */
    public HubBuild toHubBuild(){
        Collection<HubTestCase> hubTests = new ArrayList<>();
        for(TestCase t:this.getTestCases()){
            String name = t.getName();
            String ontology = t.getOntology();
            String instances = t.getInstances();
            String schema = t.getSchema();
            String producedSM = t.getProducedShapeMap();
            String expectedSM = t.getExpectedShapeMap();
            hubTests.add(new HubTestCase(name,ontology,instances,schema,producedSM,expectedSM));
        }

        Map<String,String> metaHub = Collections.unmodifiableMap(this.getMetadata());

        return HubBuild.from(hubTests,metaHub);
    }

    /**
     * Returns a Build Object clone from a HubBuild
     * @return Build
     */
    public static Build from(HubBuild hubBuild){
        Collection<TestCase> testCases = new ArrayList<>();
        for(HubTestCase t:hubBuild.getTestCases()){
            String name = t.getName();
            String ontology = t.getOntology();
            String instances = t.getInstances();
            String schema = t.getSchema();
            String producedSM = t.getProducedShapeMap();
            String expectedSM = t.getExpectedShapeMap();
            testCases.add(new TestCase(name,ontology,instances,schema,producedSM,expectedSM));
        }

        Map<String,String> metaHub = Collections.unmodifiableMap(hubBuild.getMetadata());

        return new Build(testCases,metaHub);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Build{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", testCases=" + testCases +
                '}';
    }
}
