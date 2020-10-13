package es.weso.ontoloci.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoBuildResultRepository extends MongoRepository<String, MongoBuildResult> {

}
