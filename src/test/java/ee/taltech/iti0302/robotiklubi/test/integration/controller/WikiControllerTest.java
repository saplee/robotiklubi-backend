package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;


import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    void setSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(1, null, List.of(new SimpleGrantedAuthority("MEMBER"))));
    }

    @Test
    void getPage() throws Exception {
        mvc.perform(get("/wiki/87000")).andExpect(status().isOk()).andExpect(jsonPath("$.content").value("Some content"));
    }

    @Test
    void getPageDoesNotExist() throws Exception {
        mvc.perform(get("/wiki/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPageTags() throws Exception {
        mvc.perform(get("/wiki/tags/87000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void save() throws Exception {
        WikiPageDto wikiPage = WikiPageDto.builder().title("#123.").content("#123.").build();
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage))).andExpect(status().isOk());

    }

    @Test
    void createWikiPageFaultyData() throws Exception {
        WikiPageDto wikiPage = WikiPageDto.builder().title(null).content(null).build();
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void update() throws Exception {
        WikiPageDto wikiPage = WikiPageDto.builder().title("New title").content("New content").build();
        mvc.perform(put("/wiki/update").param("id", "87001").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        mvc.perform(delete("/wiki/delete").param("id", "34000"))
                .andExpect(status().isOk());
    }

    @Test
    void searchPageByTitle() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("Some").build();
        mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.results[0].id").value(87000));
    }

    @Test
    void searchPagesByTitle() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("Some").build();
        MvcResult response = mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(response.getResponse().getContentAsString(), Map.class);
        List<WikiPageDto> results = (List<WikiPageDto>) map.get("results");
        assertEquals(3, results.size());
    }

    @Test
    void searchPagesByContent() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().contentSearch("New").build();
        MvcResult response = mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(response.getResponse().getContentAsString(), Map.class);
        List<WikiPageDto> results = (List<WikiPageDto>) map.get("results");
        assertEquals(2, results.size());
    }

    @Test
    void searchPagesByTag() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().tags(List.of(3)).build();
        MvcResult response = mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.results[0].id").value(87000))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(response.getResponse().getContentAsString(), Map.class);
        List<WikiPageDto> results = (List<WikiPageDto>) map.get("results");
        assertEquals(2, results.size());
    }

    @Test
    void searchPagesSortByTitleAscending() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("title").sortByTitle(true).sortAscending(true).build();
        mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].title").value("New title                                         "))
                .andExpect(jsonPath("$.results[-1].title").value("Some title                                        "));
    }

    @Test
    void searchPagesSortByTitleDescending() throws Exception {
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("title").sortByTitle(true).sortAscending(false).build();
        mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[-1].title").value("New title                                         "))
                .andExpect(jsonPath("$.results[0].title").value("Some title                                        "));
    }

    @Test
    void searchPagesSortByCreationDateAscending() throws Exception {
        // Create pages in defined order
        WikiPageDto wikiPage1 = WikiPageDto.builder().title("Earlier #456").content("Wasd").build();
        WikiPageDto wikiPage2 = WikiPageDto.builder().title("Later #456").content("Wasd").build();
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage1))).andExpect(status().isOk());
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage2))).andExpect(status().isOk());
        // Sort by order
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("#456")
                .sortByCreationDate(true).sortAscending(true).build();
        mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].title").value("Earlier #456                                      "))
                .andExpect(jsonPath("$.results[-1].title").value("Later #456                                        "));
    }

    @Test
    void searchPagesSortByCreationDateDescending() throws Exception {
        // Create pages in defined order
        WikiPageDto wikiPage1 = WikiPageDto.builder().title("Earlier #456").content("Wasd").build();
        WikiPageDto wikiPage2 = WikiPageDto.builder().title("Later #456").content("Wasd").build();
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage1))).andExpect(status().isOk());
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage2))).andExpect(status().isOk());
        // Sort by order
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("#456")
                .sortByCreationDate(true).sortAscending(false).build();
        mvc.perform(post("/wiki/search")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[-1].title").value("Earlier #456                                      "))
                .andExpect(jsonPath("$.results[0].title").value("Later #456                                        "));
    }

    @Test
    void searchPagesSortByEditDateAscending() throws Exception {
        // Create pages in defined order and get IDs
        WikiPageDto wikiPage1 = WikiPageDto.builder().title("Earlier #456").content("Wasd").build();
        WikiPageDto wikiPage2 = WikiPageDto.builder().title("Later #456").content("Wasd").build();
        MvcResult response1 = mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage1))).andExpect(status().isOk())
                .andReturn();
        MvcResult response2 = mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage2))).andExpect(status().isOk())
                .andReturn();
        String id1 = response1.getResponse().getContentAsString();
        String id2 = response2.getResponse().getContentAsString();
        // Update pages
        WikiPageDto wikiPage3 = WikiPageDto.builder().title("First edited #654").content("Asd").build();
        mvc.perform(put("/wiki/update").param("id", id2)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage3)));
        WikiPageDto wikiPage4 = WikiPageDto.builder().title("Second edited #654").content("Asd").build();
        mvc.perform(put("/wiki/update").param("id", id1)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage4)));
        // Sort by order
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("#654")
                .sortByEditDate(true).sortAscending(true).build();
        mvc.perform(post("/wiki/search")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].title").value("First edited #654                                 "))
                .andExpect(jsonPath("$.results[-1].title").value("Second edited #654                                "));
    }

    @Test
    void searchPagesSortByEditDateDescending() throws Exception {
        // Create pages in defined order and get IDs
        WikiPageDto wikiPage1 = WikiPageDto.builder().title("Earlier #456").content("Wasd").build();
        WikiPageDto wikiPage2 = WikiPageDto.builder().title("Later #456").content("Wasd").build();
        MvcResult response1 = mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage1))).andExpect(status().isOk())
                .andReturn();
        MvcResult response2 = mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage2))).andExpect(status().isOk())
                .andReturn();
        String id1 = response1.getResponse().getContentAsString();
        String id2 = response2.getResponse().getContentAsString();
        // Update pages
        WikiPageDto wikiPage3 = WikiPageDto.builder().title("First edited #654").content("Asd").build();
        mvc.perform(put("/wiki/update").param("id", id2)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage3)));
        WikiPageDto wikiPage4 = WikiPageDto.builder().title("Second edited #654").content("Asd").build();
        mvc.perform(put("/wiki/update").param("id", id1)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage4)));
        // Sort by order
        WikiSearchCriteria searchCriteria = WikiSearchCriteria.builder().titleSearch("#654")
                .sortByEditDate(true).sortAscending(false).build();
        mvc.perform(post("/wiki/search")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[-1].title").value("First edited #654                                 "))
                .andExpect(jsonPath("$.results[0].title").value("Second edited #654                                "));
    }
}
