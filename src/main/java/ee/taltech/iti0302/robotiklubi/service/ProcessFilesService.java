package ee.taltech.iti0302.robotiklubi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class ProcessFilesService {

    Logger logger = Logger.getLogger("ProcessFilesService");

    public void processFiles(MultipartFile file) {
        try {
            String uploadsFolder = "/uploads/";
            String fileName = file.getOriginalFilename();
            if (fileName == null) return;

            fileName = fileName.replace(" ", "_");
            String gcodeFileName = fileName.substring(0, fileName.length() - 4) + ".gcode";

            Path basePath = Paths.get("");
            File localFile = new File(basePath.toAbsolutePath() + uploadsFolder + fileName);
            file.transferTo(localFile);

            Runtime rt = Runtime.getRuntime();
            String[] command = {"curaengine slice -j /opt/Cura/resources/definitions/RobotiklubiConf.def.json -s roofing_layer_count=2 -s roofing_monotonic=true" + " -l " + uploadsFolder + fileName + " -o " + uploadsFolder + gcodeFileName};
            Process pr = rt.exec(command);

            logOutputs(pr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logOutputs(Process pr) throws IOException {
        BufferedReader bfr = new BufferedReader(new java.io.InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = bfr.readLine()) != null) {
            logger.log(Level.INFO, line);
        }
    }
}
