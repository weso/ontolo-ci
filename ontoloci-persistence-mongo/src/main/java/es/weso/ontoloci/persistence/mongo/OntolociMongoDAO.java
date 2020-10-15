package es.weso.ontoloci.persistence.mongo;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import es.weso.ontoloci.persistence.OntolociDAO;
import es.weso.ontoloci.worker.build.BuildResult;
import es.weso.ontoloci.worker.test.TestCaseResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OntolociMongoDAO implements OntolociDAO {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(OntolociMongoDAO.class);

    // MONGO SPECIFIC FIELDS
    private static final String MONGO_DB_NAME = "ontoloci";
    private static final String MONGO_COLLECTION_NAME = "build-results";

    // Specific mongo properties.
    private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection collection;

    public OntolociMongoDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.init();
    }

    private void init() {
        // If the database is not created it will create one.
        database = mongoClient.getDatabase(MONGO_DB_NAME);

        // If the collection does not exist will create one.
        collection = database.getCollection(MONGO_COLLECTION_NAME);
    }

    @Override
    public List<BuildResult> findAllBuildResults() {
        LOGGER.debug("Getting all the build results from mongo persistence layer");
        final List<Document> dbObjects = new ArrayList<>();
        collection.find().into(dbObjects);

        final List<BuildResult> buildResults = new ArrayList<>();
        BuildResult tmpBuildResult = BuildResult.from();
        for(Document dbObject: dbObjects) {
            tmpBuildResult.setId(dbObject.get("_id").toString());
            tmpBuildResult.addTestCaseResults((List<TestCaseResult>) dbObject.get("testCaseResults"));
            buildResults.add(tmpBuildResult);
        }

        return buildResults;
    }

    @Override
    public Optional<BuildResult> findBuildResultForId(String id) {
        LOGGER.debug("Getting a build results from mongo persistence layer for id " + id);
        Bson filter = eq("_id", new ObjectId(id));
        final List<Document> dbObjects = new ArrayList<>();
        collection.find(filter).into(dbObjects);

        if(dbObjects.size() > 1) {
            throw new IllegalStateException("More than one element found for the given id");
        } else if(dbObjects.isEmpty()) {
            return Optional.empty();
        } else {
            BuildResult tmpBuildResult = BuildResult.from();
            tmpBuildResult.setId(dbObjects.get(0).get("_id").toString());
            tmpBuildResult.addTestCaseResults((List<TestCaseResult>) dbObjects.get(0).get("testCaseResults"));
            return Optional.of(tmpBuildResult);
        }

    }

    @Override
    public void save(BuildResult buildResult) {
        LOGGER.debug("Saving a build result from mongo persistence layer " + buildResult);
        Document doc = new Document();
        doc.append("testCaseResults", buildResult.getTestCaseResults());
        this.collection.insertOne(doc);
    }

    @Override
    public void update(BuildResult buildResult) {
        LOGGER.debug("Updating a build result from mongo persistence layer");
        Bson filter = eq("_id", new ObjectId(buildResult.getId()));

        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.append("$set",
                new BasicDBObject().append("testCaseResults", buildResult.getTestCaseResults()));

        collection.updateOne(filter, updateQuery);
    }

    @Override
    public void remove(BuildResult buildResult) {
        LOGGER.debug("Removing a build result from mongo persistence layer for id " +buildResult.getId());

        if(buildResult.getId().isEmpty()) {
            throw new IllegalStateException("The build result object must have an id in order to remove it from the collection");
        }

        collection.deleteOne(eq("_id", new ObjectId(buildResult.getId())));
    }

    @Override
    public void removeAll() {
        LOGGER.debug("Removing all build results from mongo persistence layer for id ");
        collection.drop();
    }

}
