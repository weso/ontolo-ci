package es.weso.ontoloci.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import es.weso.ontoloci.persistence.mongo.BuildResultMongoObjectRepository;
import es.weso.ontoloci.persistence.mongo.OntolociMongoDAO;
import es.weso.ontoloci.persistence.mongo.OntolociMongoSprinbootDAO;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class OntolociMongoDAOTest {

    @Autowired
    private BuildResultMongoObjectRepository repo;

    // Mongo credentials for mlab
    private final MongoCredential credential = MongoCredential.createCredential("root", "ontoloci", "a123456".toCharArray());

    // DAO to test.
    private final OntolociDAO dao = new OntolociMongoSprinbootDAO(repo);
        //new OntolociMongoDAO(new MongoClient(new ServerAddress("ds241489.mlab.com", 41489), Arrays.asList(credential)));

    // Test instances.
    private final BuildResult r1 = BuildResult.from();
    private final BuildResult r2 = BuildResult.from();
    private final BuildResult r3 = BuildResult.from();

    @Test
    public void saveTest() {
        // First we ensure the persistence has no elements.
        //assertEquals(0, dao.findAllBuildResults().size());

        // Insert 1 element.
        dao.save(r1);

        // We check that the dao layer is storing the object correctly
        assertEquals(1, dao.findAllBuildResults().size());
        //assertEquals(r1, dao.findBuildResultForId(1).get());

        // Finally we remove the element from the dao layer.
        for(BuildResult r : dao.findAllBuildResults()) {
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

        List<BuildResult> dbResults = dao.findAllBuildResults();
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

        List<TestCaseResult> testCases = new ArrayList<>();
        testCases.add(TestCaseResult.from(new TestCase("name", "", "", "", "", "")));

        r1.addTestCaseResults(testCases);

        dao.update(r1);

        for(BuildResult buildResult: dao.findAllBuildResults()) {
            System.out.println(buildResult);
        }

        dao.removeAll();
    }
}
