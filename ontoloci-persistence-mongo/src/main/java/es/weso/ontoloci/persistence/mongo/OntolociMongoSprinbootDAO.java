package es.weso.ontoloci.persistence.mongo;

import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.worker.build.BuildResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OntolociMongoSprinbootDAO implements OntolociDAO {

    private BuildResultMongoObjectRepository repository;

    public OntolociMongoSprinbootDAO(@Autowired BuildResultMongoObjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BuildResult> findAllBuildResults() {
        return repository.findAll().stream().map(
                item -> item.asBuildResult()).collect(Collectors.toList()
        );
    }

    @Override
    public Optional<BuildResult> findBuildResultForId(String id) {
        return Optional.of(repository.findById(id).get().asBuildResult());
    }

    @Override
    public void save(BuildResult buildResult) {
        this.repository.save(BuildResultMongoObject.from(buildResult));
    }

    @Override
    public void update(BuildResult buildResult) {
        this.save(buildResult);
    }

    @Override
    public void remove(BuildResult buildResult) {
        repository.delete(BuildResultMongoObject.from(buildResult));
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
    }
}
