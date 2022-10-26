package ee.taltech.iti0302.robotiklubi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    public String saveToDisk(MultipartFile file) throws IOException {
        File localFile = new File(Paths.get("").toAbsolutePath() + "/uploads/" + file.getOriginalFilename());
        String fileName  = file.getOriginalFilename();
        int fileLength = file.getBytes().length;
        file.transferTo(localFile);
        return "File uploaded successfully, " + "file name is "+ fileName + " and length is " + fileLength + " bytes.";
    }
}
