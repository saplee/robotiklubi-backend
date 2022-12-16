package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

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



}
