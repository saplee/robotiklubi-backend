package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import ee.taltech.iti0302.robotiklubi.repository.WikiRepository;
import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;
import static sun.java2d.cmm.ProfileDataVerifier.verify;


@AutoConfigureMockMvc
@SpringBootTest
class WikiControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Mock
    private WikiRepository wikiRepository;

    @BeforeEach
    void setSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(1, null, List.of(new SimpleGrantedAuthority("MEMBER"))));
    }

    @Test
    void getPage() throws Exception {
        mvc.perform(get("/wiki/87000")).andExpect(status().isOk()).andExpect(jsonPath("$.content").value("New content"));
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
        mvc.perform(post("/wiki/search").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchCriteria))).andExpect(status().isOk()).andExpect(jsonPath("$.results[0].id").value(12345));
    }
    @Test
    void save() throws Exception {
        WikiPageDto wikiPage = WikiPageDto.builder().title("Some title 123.").content("Some content 123.").build();
        mvc.perform(post("/wiki/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wikiPage))).andExpect(status().isOk());

    }
    @Test
    void update() throws Exception {
        WikiPageDto wikiPage = WikiPageDto.builder().title("New title").content("New content").build();
        mvc.perform(put("/wiki/update").param("id", "87000").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(wikiPage)))
                .andExpect(status().isOk());
    }
    @Test
    void testDelete() throws Exception {
        mvc.perform(delete("/wiki/delete").param("id", "34000"))
                .andExpect(status().isOk());
    }
}
