package es.weso.ontoloci.persistence;

import es.weso.ontoloci.worker.build.BuildResult;

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
    List<BuildResult> findAllBuildResults();

    /**
     * Finds a build result for each unique id. It returns an optional so if none found
     * will return empty.
     *
     * @param id of the build result to find.
     * @return an optional that might contain the build result.
     */
    Optional<BuildResult> findBuildResultForId(long id);

    /**
     * Saves a build result.
     *
     * @param buildResult to save.
     */
    void save(BuildResult buildResult);

    /**
     * Updates a build result. It will persist the values from the passed buildResult to the passed id.
     *
     * @param id to update.
     * @param buildResult to attach.
     */
    void update(long id, BuildResult buildResult);
}
