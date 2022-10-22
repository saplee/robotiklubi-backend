package ee.taltech.iti0302.robotiklubi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName  = file.getOriginalFilename();
        int fileLength = file.getBytes().length;
        return "File uploaded successfully, " + "file name is "+ fileName + " and length is " + fileLength;
    }

}
