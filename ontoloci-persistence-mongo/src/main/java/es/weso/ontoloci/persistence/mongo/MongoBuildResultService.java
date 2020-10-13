package es.weso.ontoloci.persistence.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoBuildResultService {

    private MongoBuildResultRepository repository;

    @Autowired
    public MongoBuildResultService(MongoBuildResultRepository repository) {
        this.repository = repository;
    }
}
