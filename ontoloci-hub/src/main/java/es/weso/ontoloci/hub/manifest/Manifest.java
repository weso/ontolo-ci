package es.weso.ontoloci.hub.manifest;

import es.weso.ontoloci.worker.test.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A manifest represents the description of a set of manifest entries.
 *
 * @author Guillermo Facundo Colunga, Pablo Menéndez Suárez
 * @version 20201007
 */
public class Manifest {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(Manifest.class);

    private Collection<ManifestEntry> manifestEntries;

    /**
     * This constructor for the manifest file initializes the manifest entries collection to an empty array list.
     *
     * @param manifestEntries is the manifest entries collection to initialize the manifest.
     */
    public Manifest(Collection manifestEntries) {
        this.manifestEntries = new ArrayList<>();

        LOGGER.debug("Creating a new manifest " + this.toString());
    }

    /**
     * Gets a copy of the manifest entries collection.
     *
     * @return a copy of the manifest entries collection.
     */
    public Collection<ManifestEntry> getmanifestEntries() {
        return Collections.unmodifiableCollection(manifestEntries);
    }

    /**
     * Sets the manifest entries collection.
     *
     * @param manifestEntries is the collection that will substitute the existing one.
     */
    public void setManifestEntries(Collection manifestEntries) {
        this.manifestEntries = manifestEntries;
    }

    /**
     * Gets the number of manifest entries that the manifest contains. This is implemented by using the size method
     * of the manifest entries collection.
     *
     * @return the number of manifest entries that the manifest contains.
     */
    public int getNumberOfManifestEntries() {
        return this.manifestEntries.size();
    }

    @Override
    public String toString() {
        return "Manifest{" +
                    "manifestEntries=" + manifestEntries + "," +
                    "n_test_cases=" + getNumberOfManifestEntries() +
                '}';
    }
}