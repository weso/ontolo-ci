package es.weso.ontoloci.validation.manifest;

import es.weso.ontoloci.validation.test.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A manifest represents the description of a set of test cases.
 *
 * @author Guillermo Facundo Colunga
 * @version 20201007
 */
public class Manifest {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(Manifest.class);

    private Collection<TestCase> testCases;

    /**
     * This constructor for tha manifest file initializes the test cases collection to an empty array list.
     *
     * @param testCases is the test cases collection to initialize the manifest.
     */
    public Manifest(Collection testCases) {
        this.testCases = new ArrayList<>();

        LOGGER.debug("Creating a new manifest " + this.toString());
    }

    /**
     * Gets a copy of the test cases collection.
     *
     * @return a copy of the test cases collection.
     */
    public Collection getTestCases() {
        return Collections.unmodifiableCollection(testCases);
    }

    /**
     * Sets the tests cases collection.
     *
     * @param testCases is the collection that will substitute the existing one.
     */
    public void setTestCases(Collection testCases) {
        this.testCases = testCases;
    }

    /**
     * Gets the number of test cases that the manifest contains. This is implemented by using the size method
     * of the test cases collection.
     *
     * @return the number of test cases that the manifest contains.
     */
    public int getNumberOfTestCases() {
        return this.testCases.size();
    }

    @Override
    public String toString() {
        return "Manifest{" +
                    "testCases=" + testCases + "," +
                    "n_test_cases=" + getNumberOfTestCases() +
                '}';
    }
}