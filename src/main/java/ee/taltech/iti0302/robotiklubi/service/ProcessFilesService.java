package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.order.OrderResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusResponseDto;
import ee.taltech.iti0302.robotiklubi.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProcessFilesService {


    public OrderResponseDto processFiles(MultipartFile file, String firstName, String lastName, String email, String phone) {
        try {
            String uploadsFolder = "/uploads/";
            String fileName = file.getOriginalFilename();
            if (fileName == null) return new OrderResponseDto(false);

            fileName = fileName.replace(" ", "_");
            String gcodeFileName = fileName.substring(0, fileName.length() - 4) + ".gcode";

            Path basePath = Paths.get("");
            File localFile = new File(basePath.toAbsolutePath() + uploadsFolder + fileName);
            file.transferTo(localFile);

            Process pr = new ProcessBuilder("curaengine", "slice", "-j", "/opt/PrinterConfigs/RobotiklubiConf.def.json",
                    "-l",
                    uploadsFolder + fileName, "-o", uploadsFolder + gcodeFileName).start();

            Map<GcodeValues, Float> data = getGcodeData(pr);

            if (!isGcodeFilePresent(gcodeFileName)) {
                throw new FileProcessingException("Gcode file not found");
            }
            return new OrderResponseDto(true);
        } catch (IOException e) {
            throw new FileProcessingException("File processing failed");
        }
    }

    private boolean isGcodeFilePresent(String fileName) {
        Path basePath = Paths.get("");
        File localFile = new File(basePath.toAbsolutePath() + "/uploads/" + fileName);
        return localFile.exists();
    }

    public StatusResponseDto processStatus(StatusRequestDto requestDto) {
        String filename = Objects.requireNonNull(requestDto.getFileName())
                .substring(0, requestDto.getFileName().length() - 4) + ".gcode";
        StatusResponseDto responseDto = isGcodeFilePresent(filename) ? new StatusResponseDto("done") : new StatusResponseDto("not done");
        responseDto.setFileName(requestDto.getFileName());
        return responseDto;
    }

    private Map<GcodeValues, Float> getGcodeData(Process pr) throws IOException {
        BufferedReader bfr = new BufferedReader(new java.io.InputStreamReader(pr.getInputStream()));
        String line;
        Map<GcodeValues, Float> data = new EnumMap<>(GcodeValues.class);
        while ((line = bfr.readLine()) != null) {
            if (line.contains("Filament (mm^3):")) {
                data.put(GcodeValues.MATERIAL_USED, Float.parseFloat(line.substring(line.lastIndexOf(":") + 1).trim()));
            } else if (line.contains("Print time (s):")) {
                data.put(GcodeValues.PRINT_TIME, Float.parseFloat(line.substring(line.lastIndexOf(":") + 1).trim()));
            } else if (line.contains("Layer count:")) {
                data.put(GcodeValues.LAYER_COUNT, Float.parseFloat(line.substring(line.lastIndexOf(":") + 1).trim()));
            } else if (line.contains("Layer height:")) {
                data.put(GcodeValues.LAYER_HEIGHT, Float.parseFloat(line.substring(line.lastIndexOf(":") + 1).trim()));
            }
        }
        return data;
    }

    enum GcodeValues {
        MATERIAL_USED,
        PRINT_TIME,
        LAYER_COUNT,
        LAYER_HEIGHT;
    }

}
