package es.weso.ontoloci.persistence;


import java.util.List;
import java.util.Optional;

/**
 * DAO definition for the ontoloci persistence.
 *
 * @author Guillermo FAcundo Colunga
 * @version 20201014
 */
public interface OntolociDAO {

    /**
     * Finds all the build results in the persistence layer.
     *
     * @return all the existing build results in the persistence layer.
     */
    List<PersistedBuildResult> findAllBuildResults();

    /**
     * Finds a build result for each unique id. It returns an optional so if none found
     * will return empty.
     *
     * @param id of the build result to find.
     * @return an optional that might contain the build result.
     */
    Optional<PersistedBuildResult> findBuildResultForId(String id);

    /**
     * Saves a build result.
     *
     * @param buildResult to save.
     */
    void save(PersistedBuildResult buildResult);

    /**
     * Updates a build result. It will persist the values from the passed buildResult to the passed id.
     *
     * @param buildResult to attach.
     */
    void update(PersistedBuildResult buildResult);

    /**
     * Removes the element that matches the id from the persistence.
     *
     * @param buildResult to remove
     */
    void remove(PersistedBuildResult buildResult);

    /**
     * Removes all the build results in the persistence layer.
     */
    void removeAll();
}
