package es.weso.ontoloci.worker;

import es.weso.ontoloci.hub.OntolociHubImplementation;
import es.weso.ontoloci.hub.build.HubBuild;
import es.weso.ontoloci.hub.repository.impl.GitHubRepositoryProvider;
import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.test.TestCase;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ResultValidation;
import es.weso.ontoloci.worker.validation.Validate;
import es.weso.shapeMaps.ResultShapeMap;
import es.weso.shapeMaps.ShapeMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkerTest {

        // Repository data example
        private final String owner = "mistermboy";
        private final String repo = "oci-test";
        private final String branch = "main";

        @Test
        public void validationTest() {

            Build build = Build.from(new ArrayList<>());
            Map<String,String> metadataExample = new HashMap<>();
            metadataExample.put("owner",owner);
            metadataExample.put("repo",repo);
            metadataExample.put("branch",branch);
            build.setMetadata(metadataExample);

            OntolociHubImplementation ontolocyHub = new OntolociHubImplementation();
            //Transform the current build to a HubBuild
            HubBuild hubBuild = build.toHubBuild();
            //Add the tests
            hubBuild = ontolocyHub.addTestsToBuild(hubBuild);
            //Transform the returned HubBuild to a Build and overwrites the result
            build = build.from(hubBuild);


            for(TestCase testCase : build.getTestCases()) {
                Validate v = new Validate();
                ResultValidation result = v.validateStrExpected(
                        testCase.getOntology(),
                        testCase.getInstances(),
                        testCase.getSchema(),
                        testCase.getProducedShapeMap(),
                        testCase.getExpectedShapeMap()).unsafeRunSync();
                assertTrue(result.getExpectedShapeMap().toJson().spaces2().length() > 0);
            }

        }

}
