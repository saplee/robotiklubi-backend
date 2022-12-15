package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302.robotiklubi.dto.user.LoginRequestDto;

import ee.taltech.iti0302.robotiklubi.dto.user.SignUpUserDto;

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
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void regularMembers() throws Exception {
        mvc.perform(get("/users/members")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void management() throws Exception {
        mvc.perform(get("/users/management")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
    @Test
    void signUp() throws Exception {
        SignUpUserDto user = SignUpUserDto.builder().lastName("K").firstName("KK").password("123").email("j.k@mail.ee").build();
        mvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andExpect(jsonPath("$.emailError").value(false));

    }
    @Test
    void signUpSameEmail() throws Exception {
        SignUpUserDto user = SignUpUserDto.builder().lastName("K").firstName("KK").password("123").email("k.k@mail.ee").build();
        mvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andExpect(jsonPath("$.emailError").value(true));

    }
    @Test
    void loginFails() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("k.k@mail.ee");
        loginRequestDto.setPassword("13321");
        mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))).andExpect(status().isOk()).andExpect(jsonPath("$.succeeded").value(false));

    }
    @Test
    void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("k.k@mail.ee");
        loginRequestDto.setPassword("abc");
        mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))).andExpect(status().isOk()).andExpect(jsonPath("$.succeeded").value(true));

    }
    @Test
    void login2() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("k.k@mail.ee");
        loginRequestDto.setPassword("abc");
        mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))).andExpect(status().isOk()).andExpect(jsonPath("$.accessToken").isString());

    }
}
