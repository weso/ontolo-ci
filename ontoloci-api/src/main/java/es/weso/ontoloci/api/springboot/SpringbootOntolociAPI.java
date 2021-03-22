package es.weso.ontoloci.api.springboot;

import es.weso.ontoloci.api.OntolociAPI;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.PersistedBuildResult;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class provides an endpoint for the ontoloci-web module.
 * Allows the front to collect all the build results and for each
 * build the result of the test cases.
 *
 * @author Guillermo Facundo Colunga, Pablo Menéndez
 */
@RestController
@CrossOrigin( value = "*")
@RequestMapping("/api/v1")
public class SpringbootOntolociAPI implements OntolociAPI {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringbootOntolociAPI.class);
    private final OntolociDAO persistence = OntolociInMemoryDAO.instance();

    /**
     * API endpoint for fetching all the build results.
     * @return build results
     */
    @GetMapping("/buildResults")
    @Override
    public ResponseEntity getAllBuildResults() {
        LOGGER.debug(String.format("GET '/buildResults' endpoint triggered, returning [%s] elements", persistence.findAllBuildResults().size()));
        return new ResponseEntity(persistence.findAllBuildResults(), HttpStatus.OK);
    }

    /**
     * API endpoint for fetching a concrete build result by an ID.
     * This ID is defined in the request path parameters.
     *
     * @return build result
     */
    @GetMapping("/buildResults/{buildId}")
    @Override
    public ResponseEntity getBuildResult(@PathVariable String buildId) {
        LOGGER.debug(String.format("GET '/buildResults/{buildId}' endpoint triggered, with buildId=[%s]", buildId));

        final PersistedBuildResult result = persistence.findBuildResultForId(buildId).orElse(PersistedBuildResult.from());

        if(result.isEmpty()) {
            LOGGER.error(String.format("no result found for buildId=[%s]", buildId));
            return new ResponseEntity("NotFound", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

}
