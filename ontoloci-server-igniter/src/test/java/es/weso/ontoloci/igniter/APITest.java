package es.weso.ontoloci.igniter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void getAllBuildsTest() throws Exception {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classloader.getResourceAsStream("github-push-event.json");
                String content = IOUtils.toString(inputStream);
                this.mockMvc.perform(
                        post("/api/v1/github/")
                                .header("X-GitHub-Event","push")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk());

                MvcResult result = this.mockMvc.perform(
                        get("/api/v1/buildResults")
                                .content(content)
                ).andExpect(status().isOk()).andReturn();

                String resContent = result.getResponse().getContentAsString();
                ArrayList resObjectList = (ArrayList) new ObjectMapper(new JsonFactory()).readValue(resContent,Object.class);
                assertTrue(resObjectList.size()>0);

        }


        @Test
        public void getSpecificFailureBuildTest() throws Exception {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classloader.getResourceAsStream("github-push-event.json");
                String content = IOUtils.toString(inputStream);
                this.mockMvc.perform(
                        post("/api/v1/github/")
                                .header("X-GitHub-Event","push")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk());

                MvcResult buildResult = this.mockMvc.perform(
                        get("/api/v1/buildResults")
                                .content(content)
                ).andExpect(status().isOk()).andReturn();

                
                String resContent = buildResult.getResponse().getContentAsString();
                ArrayList<LinkedHashMap> resObjectList = (ArrayList) new ObjectMapper(new JsonFactory()).readValue(resContent,ArrayList.class);
                assertTrue(resObjectList.size()>0);
                
                String expectedId = "";
                String expectedStatus = "";
                for(LinkedHashMap build : resObjectList){
                        if(build.get("status").equals("FAILURE")){
                                expectedId = (String) build.get("id");
                                expectedStatus = (String) build.get("status");
                        }
                }

                MvcResult result = this.mockMvc.perform(
                        get("/api/v1/buildResults/"+expectedId)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk()).andReturn();

                String build = result.getResponse().getContentAsString();
                LinkedHashMap buildObj = (LinkedHashMap) new ObjectMapper(new JsonFactory()).readValue(build,LinkedHashMap.class);

                String resId = (String) buildObj.get("id");
                String resStatus = (String) buildObj.get("status");

                assertEquals(resId,expectedId);
                assertEquals(resStatus,expectedStatus);
        }


        @Test
        public void getSpecificSuccessBuildTest() throws Exception {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classloader.getResourceAsStream("github-push-event-success.json");
                String content = IOUtils.toString(inputStream);
                this.mockMvc.perform(
                        post("/api/v1/github/")
                                .header("X-GitHub-Event","push")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk());

                MvcResult buildResult = this.mockMvc.perform(
                        get("/api/v1/buildResults")
                                .content(content)
                ).andExpect(status().isOk()).andReturn();


                String resContent = buildResult.getResponse().getContentAsString();
                ArrayList<LinkedHashMap> resObjectList = (ArrayList) new ObjectMapper(new JsonFactory()).readValue(resContent,ArrayList.class);
                assertTrue(resObjectList.size()>0);

                String expectedId = "";
                String expectedStatus = "";
                for(LinkedHashMap build : resObjectList){
                        if(build.get("status").equals("FAILURE")){
                                expectedId = (String) build.get("id");
                                expectedStatus = (String) build.get("status");
                        }
                }

                MvcResult result = this.mockMvc.perform(
                        get("/api/v1/buildResults/"+expectedId)
                                .content(content)
                ).andDo(print()).andExpect(status().isOk()).andReturn();

                String build = result.getResponse().getContentAsString();
                LinkedHashMap buildObj = (LinkedHashMap) new ObjectMapper(new JsonFactory()).readValue(build,LinkedHashMap.class);

                String resId = (String) buildObj.get("id");
                String resStatus = (String) buildObj.get("status");

                assertEquals(resId,expectedId);
                assertEquals(resStatus,expectedStatus);
        }

        @Test
        public void getSpecificBuildInvalidIDTest() throws Exception {
                this.mockMvc.perform(get("/api/v1/buildResults/0")).andDo(print()).andExpect(status().isNotFound());
        }

}
