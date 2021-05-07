package es.weso.ontoloci.listener;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

public interface RepositoryRestListener {

    void listen(@RequestHeader("X-GitHub-Event") String event, @RequestBody Map<String, Object> payload);
}
