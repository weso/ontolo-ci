package es.weso.cucumber;

import es.weso.ontoloci.OntolociIgniter;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest(classes= OntolociIgniter.class)
@AutoConfigureMockMvc
public class SpringIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    protected MvcResult executePost(String path,String githubHeader,String content) throws Exception {
        return mockMvc.perform(
                post(path)
                        .header("X-GitHub-Event",githubHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)

        ).andExpect(status().isOk()).andReturn();
    }

    protected MvcResult executeGet(String path) throws Exception {
        return mockMvc.perform(
                get(path)

        ).andExpect(status().isOk()).andReturn();
    }

}
