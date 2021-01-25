package es.weso.cucumber;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.cucumber.entities.BuildResult;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.io.IOUtils;
import org.springframework.test.web.servlet.MvcResult;
import java.io.InputStream;
import static org.junit.Assert.*;


public class StepDefinitions extends SpringIntegrationTest {

    private String owner;
    private String repository;

    @Given("the repository ontolo-ci-test of weso organization")
    public void repo_weso_ontolo_ci_test() {
        owner = "weso";
        repository = "ontolo-ci-test";
    }

    @When("I make some changes and I push the content")
    public void make_changes_and_push() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("github-push-event.json");
        String content = IOUtils.toString(inputStream);
        executePost("/api/v1/github/","push",content);
    }


    @Then("The ontology of the repository it´s validated")
    public void new_created_validated_build() throws Exception {
        MvcResult mvcResult = executeGet("/api/v1/buildResults/");
        String result = mvcResult.getResponse().getContentAsString();
        assertNotEquals(result,"[]]");
        ObjectMapper jsonMapper =  new ObjectMapper(new JsonFactory());
        BuildResult[] buildResult = jsonMapper.readValue(result, BuildResult[].class);
        String owner = buildResult[0].getMetadata().get("owner");
        String repository = buildResult[0].getMetadata().get("repo");

        assertEquals(owner,this.owner);
        assertEquals(repository,this.repository);
        assertTrue(buildResult.length>0);
    }

}
