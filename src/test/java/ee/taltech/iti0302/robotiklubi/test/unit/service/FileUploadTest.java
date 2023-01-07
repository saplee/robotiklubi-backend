package ee.taltech.iti0302.robotiklubi.test.unit.service;

import ee.taltech.iti0302.robotiklubi.service.FileUploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FileUploadTest {


    @InjectMocks
    private FileUploadService fileUploadService;

    @Test
    void saveFile() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "some xml".getBytes());
        // when
        String response = fileUploadService.saveToDisk(file);
        // then
        assertEquals("File uploaded successfully, file name is " + file.getOriginalFilename() + " and length is " +
                file.getSize() + " bytes.", response);

    }
}
