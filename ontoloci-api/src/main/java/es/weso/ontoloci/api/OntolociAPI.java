package es.weso.ontoloci.api;

import es.weso.ontoloci.persistence.PersistedBuildResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Common interface for all the ontoloci api possible implementations.
 * This is done such that you can implement the API in any framework but providing
 * this common functionality.
 *
 * @author Guillermo Facundo Colunga
 * @version 20201016
 */
public interface OntolociAPI {

    /**
     * Gets a list containing all the build results that exists in the database. Should be
     * sorted from the most recent one first.
     *
     * @return a list containing all the build results as a JSON array.
     */
    ResponseEntity getAllBuildResults();

    /**
     * Gets a build result for a given id. If the id does not exist then an empty build result is return.
     *
     * @param id of the build result that you want to retrieve.
     * @return the build result if exists, an empty one else.
     */
    ResponseEntity getBuildResult(String id);
}
