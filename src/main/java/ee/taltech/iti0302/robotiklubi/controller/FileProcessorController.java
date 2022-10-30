package ee.taltech.iti0302.robotiklubi.controller;

import ee.taltech.iti0302.robotiklubi.service.FileUploadService;
import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileProcessorController {

    @NonNull
    ProcessFilesService processFilesService;
    @NonNull
    FileUploadService fileUploadService;

    @PostMapping("/process")
    public String processFiles(@RequestParam("file") MultipartFile file) throws IOException {
        fileUploadService.saveToDisk(file);
        processFilesService.processFiles(file);
        return "Done process";
    }
}
