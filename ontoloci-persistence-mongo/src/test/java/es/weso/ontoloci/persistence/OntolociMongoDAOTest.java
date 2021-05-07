package es.weso.ontoloci.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.weso.ontoloci.persistence.mongo.OntolociInMemoryDAO;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OntolociMongoDAOTest {


    // DAO to test.
    private final OntolociDAO dao = OntolociInMemoryDAO.instance(); //new OntolociMongoSprinbootDAO(repo);
    //new OntolociMongoDAO(new MongoClient(new ServerAddress("ds241489.mlab.com", 41489), Arrays.asList(credential)));

    // Test instances.
    private final PersistedBuildResult r1 = PersistedBuildResult.from(UUID.randomUUID().toString(),new HashMap<>(),new ArrayList<>());
    private final PersistedBuildResult r2 = PersistedBuildResult.from(UUID.randomUUID().toString(),new HashMap<>(),new ArrayList<>());
    private final PersistedBuildResult r3 = PersistedBuildResult.from(UUID.randomUUID().toString(),new HashMap<>(),new ArrayList<>());

    @Test
    public void saveTest() {
        // First we ensure the persistence has no elements.
        assertEquals(0, dao.findAllBuildResults().size());

        // Insert 1 element.
        dao.save(r1);

        // We check that the dao layer is storing the object correctly
        assertEquals(1, dao.findAllBuildResults().size());
        assertEquals(r1, dao.findBuildResultForId(r1.getId()).get());

        // Finally we remove the element from the dao layer.
        for(PersistedBuildResult r : dao.findAllBuildResults()) {
            System.out.println(r);
            dao.remove(r);
        }
        assertEquals(0, dao.findAllBuildResults().size());
    }

    @Test
    public void findAllBuildResultsTest() {
        // First we ensure the persistence has no elements.
        assertEquals(0, dao.findAllBuildResults().size());

        // We add one element and check that it is inserted.
        dao.save(r1);
        assertEquals(1, dao.findAllBuildResults().size());

        // We add a second element and check that both elements are return
        dao.save(r2);
        assertEquals(2, dao.findAllBuildResults().size());

        // We add one more element and check that all 3 are return.
        dao.save(r3);
        assertEquals(3, dao.findAllBuildResults().size());

        // Finally we remove all elements.
        dao.removeAll();
        assertEquals(0, dao.findAllBuildResults().size());
    }

    @Test
    public void findBuildResultForIdTest() {
        // First we ensure the persistence has no elements.
        assertEquals(0, dao.findAllBuildResults().size());

        // Insert 3 elements.
        dao.save(r1);
        dao.save(r2);
        dao.save(r3);

        assertEquals(3, dao.findAllBuildResults().size());

        List<PersistedBuildResult> dbResults = dao.findAllBuildResults();
        assertEquals(dbResults.get(0).getId(), dao.findBuildResultForId(dbResults.get(0).getId()).get().getId());
        assertEquals(dbResults.get(1).getId(), dao.findBuildResultForId(dbResults.get(1).getId()).get().getId());
        assertEquals(dbResults.get(2).getId(), dao.findBuildResultForId(dbResults.get(2).getId()).get().getId());

        dao.removeAll();
        assertEquals(0, dao.findAllBuildResults().size());
    }

    @Test
    public void updateTest() {
        // First we ensure the persistence has no elements.
        assertEquals(0, dao.findAllBuildResults().size());

        // Insert 3 elements.
        dao.save(r1);

        List<PersistedTestCaseResult> testResultCases = new ArrayList<>();
        testResultCases.add(PersistedTestCaseResult.from(new PersistedTestCase("name", "", "", "", "", "")));

        r1.addTestCaseResults(testResultCases);

        dao.update(r1);

        for(PersistedBuildResult buildResult: dao.findAllBuildResults()) {
            System.out.println(buildResult);
        }

        dao.removeAll();
    }
}