package es.weso.ontoloci.igniter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.io.InputStream;


@SpringBootTest
@AutoConfigureMockMvc
public class ListenerTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void notGitHubHeaderTest() throws Exception {
            this.mockMvc.perform(post("/api/v1/github/")).andDo(print()).andExpect(status().isBadRequest());
        }

        @Test
        public void notGitHubBodyTest() throws Exception {
                this.mockMvc.perform(post("/api/v1/github/").header("X-GitHub-Event","push"))
                        .andDo(print()).andExpect(status().isBadRequest());
        }


        @Test
        public void GitHubPushTest() throws Exception {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classloader.getResourceAsStream("github-push-event.json");
                String content = IOUtils.toString(inputStream);
                this.mockMvc.perform(
                        post("/api/v1/github/")
                                .header("X-GitHub-Event","push")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk());
        }

        @Test
        public void GitHubPullTest() throws Exception {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classloader.getResourceAsStream("github-pull-event.json");
                String content = IOUtils.toString(inputStream);
                this.mockMvc.perform(
                        post("/api/v1/github/")
                                .header("X-GitHub-Event","pull_request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk());
        }
}
