package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302.robotiklubi.dto.user.LoginRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.user.RefreshRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.user.SignUpUserDto;

import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;
import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
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

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        mvc.perform(get("/users/members")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
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
    void signUpInvalidUserData() throws Exception {
        SignUpUserDto user = SignUpUserDto.builder().lastName(null).firstName(null).password("123").email("j.k@mail.ee").build();
        mvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isInternalServerError());

    }

    @Test
    void signUpSameEmail() throws Exception {
        SignUpUserDto user = SignUpUserDto.builder().lastName("K").firstName("KK").password("123").email("k.k@mail.ee").build();
        mvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andExpect(jsonPath("$.emailError").value(true));

    }

    @Test
    void testLoginFails() throws Exception {
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

    @Test
    void tokenRefresh() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("k.k@mail.ee");
        loginRequestDto.setPassword("abc");
        // Get original tokens
        MvcResult response = mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk()).andReturn();
        // Extract token from response
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(response.getResponse().getContentAsString(), Map.class);
        String oldAccessToken = (String) map.get("accessToken");
        String oldRefreshToken = (String) map.get("refreshToken");
        System.out.println(oldAccessToken);
        System.out.println(oldRefreshToken);
        // Refresh the token
        sleep(1000L);
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto();
        refreshRequestDto.setAccessToken(oldAccessToken);
        MvcResult refreshResponse = mvc.perform(post("/user/refresh").header("Authorization", oldRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequestDto)))
                .andExpect(status().isOk()).andReturn();
        // Check response
        Map<String, Object> map2 = mapper.readValue(refreshResponse.getResponse().getContentAsString(), Map.class);
        System.out.println((String) map2.get("accessToken"));
        System.out.println((String) map2.get("refreshToken"));
    }

    @Test
    void getUserInfo() throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(8000, null, List.of(new SimpleGrantedAuthority("MEMBER"), new SimpleGrantedAuthority("USER"))));
        mvc.perform(get("/users/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("K"))
                .andExpect(jsonPath("$.lastName").value("K"))
                .andExpect(jsonPath("$.email").value("k.k@mail.ee                                       "))
                .andExpect(jsonPath("$.role").value(2));
    }
    @Test
    void testUserUpdate() throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDto userDto = UserDto.builder().email("ef@mail.ee").lastName("New").firstName("Name").phone("1234").build();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(80000, null, List.of(new SimpleGrantedAuthority("MEMBER"), new SimpleGrantedAuthority("USER"))));
        mvc.perform(put("/user/update").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }
}
