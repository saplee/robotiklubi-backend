package ee.taltech.iti0302.robotiklubi.scheduler;

import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FileProcessorScheduler {

    private static final int DELAY_BETWEEN_PROCESSES = 5000;

    private final ProcessFilesService processFilesService;

    @Scheduled(fixedDelay = DELAY_BETWEEN_PROCESSES)
    public void processFiles() {
        processFilesService.processFiles();
    }
}
