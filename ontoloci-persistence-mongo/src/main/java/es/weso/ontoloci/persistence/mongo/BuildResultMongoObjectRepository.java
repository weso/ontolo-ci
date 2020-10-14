package es.weso.ontoloci.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildResultMongoObjectRepository extends MongoRepository<BuildResultMongoObject, String> {
}
