package es.weso.ontoloci.persistence.mongo;

import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.worker.build.BuildResult;

import java.util.*;

public class OntolociInMemoryDAO implements OntolociDAO {

    private final Map<String, BuildResult> db = new HashMap<>();

    @Override
    public List<BuildResult> findAllBuildResults() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<BuildResult> findBuildResultForId(String id) {
        return Optional.of(db.get(id));
    }

    @Override
    public void save(BuildResult buildResult) {
        if(Objects.isNull(buildResult.getId())) {
            buildResult.setId(Long.toString(System.nanoTime()));
        }
        db.put(buildResult.getId(), buildResult);
    }

    @Override
    public void update(BuildResult buildResult) {
        this.save(buildResult);
    }

    @Override
    public void remove(BuildResult buildResult) {
        db.remove(buildResult.getId());
    }

    @Override
    public void removeAll() {
        db.clear();
    }
}
