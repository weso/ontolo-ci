package es.weso.ontoloci.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import es.weso.ontoloci.persistence.mongo.OntolociMongoDAO;
import es.weso.ontoloci.worker.build.BuildResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class OntolociMongoDAOTest {

    private final MongoCredential credential = MongoCredential.createCredential("root", "ontoloci", "a123456".toCharArray());

    // DAO to test.
    private final OntolociDAO dao = new OntolociMongoDAO(new MongoClient(new ServerAddress("ds241489.mlab.com", 41489), Arrays.asList(credential)));

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
        assertEquals(r1, dao.findBuildResultForId(1).get());

        // finally we remove the element from the dao layer.
        dao.remove(r1);
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
}
