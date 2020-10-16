package es.weso.ontoloci;

import es.weso.ontoloci.listener.GitHubRestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OntolociIgniter {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRestListener.class);

    public static void main(String... args) {
        SpringApplication.run(OntolociIgniter.class, args);
    }

}
