package es.weso.ontoloci.hub.manifest;

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
        this.manifestEntries = new ArrayList<>(manifestEntries);
        LOGGER.debug(String.format("Creating new Manifest from the public constructor with [%s] manifest entries", manifestEntries.size()));
    }

    /**
     * Gets a copy of the manifest entries collection.
     *
     * @return a copy of the manifest entries collection.
     */
    public Collection<ManifestEntry> getManifestEntries() {
        LOGGER.debug(String.format("GET reading the manifest entries of the Manifest, returning [%s] elements", this.manifestEntries.size()));
        return Collections.unmodifiableCollection(manifestEntries);
    }

    /**
     * Sets the manifest entries collection.
     *
     * @param manifestEntries is the collection that will substitute the existing one.
     */
    public void setManifestEntries(Collection<ManifestEntry> manifestEntries) {
        LOGGER.debug(String.format("SET writing the manifest entries of the Manifest, new collections has size of [%s]", manifestEntries.size()));
        this.manifestEntries = manifestEntries;
    }

    /**
     * Gets the number of manifest entries that the manifest contains. This is implemented by using the size method
     * of the manifest entries collection.
     *
     * @return the number of manifest entries that the manifest contains.
     */
    public int getNumberOfManifestEntries() {
        LOGGER.debug(String.format("GET reading the number of manifest entries of the Manifest, returning [%s]", this.manifestEntries.size()));
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