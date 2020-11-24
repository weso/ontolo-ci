package es.weso.ontoloci.api.springboot;

import es.weso.ontoloci.api.OntolociAPI;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.PersistedBuildResult;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin( value = "*")
@RequestMapping("/api/v1")
public class SpringbootOntolociAPI implements OntolociAPI {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringbootOntolociAPI.class);

    private final OntolociDAO persistence = OntolociInMemoryDAO.instance();


    @GetMapping("/buildResults")
    @Override
    public List<PersistedBuildResult> getAllBuildResults() {
        LOGGER.debug(
                String.format(
                        "GET '/buildResults' endpoint triggered, returning [%s] elements",
                        persistence.findAllBuildResults().size()
                )
        );

        return persistence.findAllBuildResults();
    }


    @GetMapping("/buildResults/{buildId}")
    @Override
    public PersistedBuildResult getBuildResult(@PathVariable String buildId) {
        LOGGER.debug(
                String.format("GET '/buildResults/{buildId}' endpoint triggered, with buildId=[%s]", buildId)
        );

        final PersistedBuildResult result = persistence.findBuildResultForId(buildId).orElse(PersistedBuildResult.from());

        // If the object is not found in the persistence layer then log this as this should not happen.
        if(Objects.isNull(result.getId())) {
            LOGGER.error(String.format("no result found for buildId=[%s]", buildId));
        }

        return persistence.findBuildResultForId(buildId).orElse(PersistedBuildResult.from());
    }

}
