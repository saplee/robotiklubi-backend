package ee.taltech.iti0302.robotiklubi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Transactional
@RequiredArgsConstructor
@Service
public class ProcessFilesService {

    private final Logger logger = Logger.getLogger("ProcessFilesService");

    public boolean processFiles(MultipartFile file) {
        try {
            String uploadsFolder = "/uploads/";
            String fileName = file.getOriginalFilename();
            if (fileName == null) return false;

            fileName = fileName.replace(" ", "_");
            String gcodeFileName = fileName.substring(0, fileName.length() - 4) + ".gcode";

            Path basePath = Paths.get("");
            File localFile = new File(basePath.toAbsolutePath() + uploadsFolder + fileName);
            file.transferTo(localFile);

            Runtime rt = Runtime.getRuntime();
            String[] command = {"curaengine slice -j /opt/PrinterConfigs/RobotiklubiConf.def.json" +
                    " -l " + uploadsFolder + fileName + " -o " +
                    uploadsFolder + gcodeFileName};
            Process pr = new ProcessBuilder("curaengine", "slice", "-j", "/opt/PrinterConfigs/RobotiklubiConf.def.json", "-l",
                    uploadsFolder + fileName, "-o", uploadsFolder + gcodeFileName).start();

            logOutputs(pr);
            return isGcodeFilePresent(gcodeFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void logOutputs(Process pr) throws IOException {
        BufferedReader bfr = new BufferedReader(new java.io.InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = bfr.readLine()) != null) {
            logger.log(Level.INFO, line);
        }
    }

    private boolean isGcodeFilePresent(String fileName) {
        Path basePath = Paths.get("");
        File localFile = new File(basePath.toAbsolutePath() + "/uploads/" + fileName);
        return localFile.exists();
    }
}
