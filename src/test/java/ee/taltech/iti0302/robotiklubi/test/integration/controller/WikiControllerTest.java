package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;
import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class WikiControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    @Test
    void getPage() throws Exception {
        mvc.perform(get("/wiki/87000")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Some title                                        "));

    }

    @Test
    void saveFails() throws Exception {
        WikiPageDto wikiPage1 = WikiPageDto.builder().title("Some title").id(1L).content("Some content").build();
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage1))).andExpect(status().isForbidden());

    }

    @Test
    void getPageTags() throws Exception {
        mvc.perform(get("/wiki/tags/87000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPageBySearch() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("Some").build();
        mvc.perform(post("/wiki/search").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria))).andExpect(status().isOk()).andExpect(jsonPath("$.results[0].id").value(87000));
    }
//    @Test
//    void save() throws Exception {
//        WikiPageDto wikiPage = WikiPageDto.builder().title("Some title 123.").content("Some content 123.").build();
//        mvc.perform(post("/wiki/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(wikiPage))).andExpect(status().isOk());
//
//    }
//    @Test
//    void update() throws Exception {
//        WikiPageDto wikiPage = WikiPageDto.builder().title("New title").content("New content").build();
//        mvc.perform(put("/wiki/edit").param("id", "87000").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage)))
//                .andExpect(status().isOk());
//    }
    @Test
    void deleteFails() throws Exception {
        mvc.perform(delete("/wiki/delete").param("id", "87000"))
                .andExpect(status().isForbidden());
    }
//    @Test
//    void delete() throws Exception {
//        mvc.perform(put("/wiki/delete").param("id", "87000"))
//                .andExpect(status().isOk());
//    }
}
