package es.weso.ontoloci.persistence.mongo;

import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.persistence.PersistedBuildResult;

import java.util.*;

public class OntolociInMemoryDAO implements OntolociDAO {

    private static final OntolociInMemoryDAO INSTANCE = new OntolociInMemoryDAO();

    private final Map<String, PersistedBuildResult> db = new HashMap<>();

    public static OntolociInMemoryDAO instance() {
        return INSTANCE;
    }

    private OntolociInMemoryDAO() {}

    @Override
    public List<PersistedBuildResult> findAllBuildResults() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<PersistedBuildResult> findBuildResultForId(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void save(PersistedBuildResult buildResult) {
        if(Objects.isNull(buildResult.getId())) {
            buildResult.setId(Long.toString(System.nanoTime()));
        }
        db.put(buildResult.getId(), buildResult);
    }

    @Override
    public void update(PersistedBuildResult buildResult) {
        this.save(buildResult);
    }

    @Override
    public void remove(PersistedBuildResult buildResult) {
        db.remove(buildResult.getId());
    }

    @Override
    public void removeAll() {
        db.clear();
    }
}
