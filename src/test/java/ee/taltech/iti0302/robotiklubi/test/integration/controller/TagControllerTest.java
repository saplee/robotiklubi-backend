package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class TagControllerTest extends AbstractIntegrationTest {

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
    void allTags() throws Exception {
        mvc.perform(get("/tags/all")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void createNewTag() throws Exception {
        TagDto tagDto = TagDto.builder().tag("Gaming").build();
        mvc.perform(post("/tags/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tagDto))).andExpect(status().isOk());
    }

    @Test
    void createDuplicateTag() throws Exception {
        TagDto tagDto = TagDto.builder().tag("Game").build();
        mvc.perform(post("/tags/create")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tagDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTag() throws Exception {
        mvc.perform(delete("/tags/delete").param("id", "900")).andExpect(status().isOk());
    }

    @Test
    void testUpdateTag() throws Exception {
        TagDto tagDto = TagDto.builder().tag("Pizza").build();
        mvc.perform(put("/tags/update").param("id", "3").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDto))).andExpect(status().isOk());
    }
}
