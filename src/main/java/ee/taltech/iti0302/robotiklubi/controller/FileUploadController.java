package ee.taltech.iti0302.robotiklubi.controller;

import com.sun.istack.NotNull;
import ee.taltech.iti0302.robotiklubi.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    @NotNull
    FileUploadService fileUploadService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileUploadService.saveToDisk(file);
    }
}
