package es.weso.ontoloci.api.springboot;

import es.weso.ontoloci.api.OntolociAPI;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import es.weso.ontoloci.worker.build.BuildResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SpringbootOntolociAPI implements OntolociAPI {

    private final OntolociDAO persistence = new OntolociInMemoryDAO();

    @GetMapping("/buildResults")
    @Override
    public List<BuildResult> getAllBuildResults() {
        return persistence.findAllBuildResults();
    }

    @GetMapping("/buildResults/{buildId}")
    @Override
    public BuildResult getBuildResult(@PathVariable String buildId) {
        return persistence.findBuildResultForId(buildId).orElse(BuildResult.from());
    }
}
