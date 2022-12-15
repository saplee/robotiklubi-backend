package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
 class TagControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void allTags() throws Exception {
        mvc.perform(get("/tags/all")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }
   /* @Test
    void createNewTag() throws Exception {
        TagDto tagDto = TagDto.builder().tag("Gaming").build();
        mvc.perform(post("/tags/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tagDto))).andExpect(status().isOk());
    }*/

   /* @Test
    void delete() throws Exception {
        mvc.perform(post("/tags/delete").param("id", "3")).andExpect(status().isOk());
    }*/


}
