package ee.taltech.iti0302.robotiklubi.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ee.taltech.iti0302.robotiklubi.dto.order.StatusRequestDto;
import ee.taltech.iti0302.robotiklubi.test.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class FileProcessControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void correctFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.stl", "application/sla", "test".getBytes());
        String firstName = "FName";
        String lastName = "LName";
        String email = "email";
        String phone = "12345678";
        mvc.perform(multipart("/process")
                .file(file)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void incorrectFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        String firstName = "FName";
        String lastName = "LName";
        String email = "email";
        String phone = "12345678";
        mvc.perform(multipart("/process")
                .file(file)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(status().isOk());
    }

    @Test
    void statusChecking() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "application/sla", "test".getBytes());
        String firstName = "FName";
        String lastName = "LName";
        String email = "email";
        String phone = "12345678";
        mvc.perform(multipart("/process")
                .file(file)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone))
                .andExpect(status().isOk());
        StatusRequestDto statusRequestDto = StatusRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .fileName(file.getOriginalFilename())
                .build();
        wait();
        mvc.perform(post("/process/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequestDto)))
                .andExpect(status().isOk());
    }


}
