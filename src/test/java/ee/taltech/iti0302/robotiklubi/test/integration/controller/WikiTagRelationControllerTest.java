package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.TagListDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;

import ee.taltech.iti0302.robotiklubi.repository.WikiRepository;
import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class WikiTagRelationControllerTest extends AbstractIntegrationTest {

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
    void tagRelation() throws Exception {
        mvc.perform(post("/tags/relation/create?pageId=12345&tagId=66")).andExpect(status().isOk());
    }

    @Test
    void tagRelationMAny() throws Exception {
        TagListDto tagListDto = new TagListDto();
        TagDto tagDto = TagDto.builder().tag("Tank").id(123L).build();
        TagDto tagDto2 = TagDto.builder().tag("Plane").id(1234L).build();
        List<TagDto> tagDtoList = new ArrayList<>(List.of(tagDto2, tagDto));
        tagListDto.setTags(tagDtoList);
        mvc.perform(post("/tags/relation/create/many?pageId=12345").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tagListDto))).andExpect(status().isOk());
    }

    @Test
    void deleteTag() throws Exception {
        TagListDto tagListDto = new TagListDto();
        TagDto tagDto = TagDto.builder().tag("Tank").id(123L).build();
        TagDto tagDto2 = TagDto.builder().tag("Plane").id(1234L).build();
        List<TagDto> tagDtoList = new ArrayList<>(List.of(tagDto2, tagDto));
        tagListDto.setTags(tagDtoList);
        mvc.perform(delete("/tags/relation/delete?pageId=12345&tagId=3")).andExpect(status().isOk());
    }
}
