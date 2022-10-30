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
            Path basePath = Paths.get("");
            File localFile = new File(basePath.toAbsolutePath() + "/uploads/" + file.getOriginalFilename());
            String outputPath = localFile.toString().substring(0, localFile.toString().length() - 4) + ".gcode";

            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("CuraEngine slice -j /home/nevermore/Documents/Robotiklubi-Server/cura/Cura/resources/definitions/RobotiklubiConf.def.json -s roofing_layer_count=2 -s roofing_monotonic=true" + " -l " + localFile.getPath() + " -o " + outputPath);

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
