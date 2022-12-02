package ee.taltech.iti0302.robotiklubi.controller.file;

import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileProcessorController {

    private final ProcessFilesService processFilesService;

    @PostMapping("/process")
    public String processFiles(@RequestParam("file") MultipartFile file) {
        return processFilesService.processFiles(file) ? "Done process" : "Error";
    }
}
