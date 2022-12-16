package ee.taltech.iti0302.robotiklubi.controller.file;

import ee.taltech.iti0302.robotiklubi.dto.order.OrderResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusResponseDto;
import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileProcessorController {

    private final ProcessFilesService processFilesService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            value = "/process")
    public OrderResponseDto processFiles(@RequestParam("file") MultipartFile file,
                                         @RequestParam("firstName") String firstName,
                                         @RequestParam("lastName") String lastName,
                                         @RequestParam("email") String email,
                                         @RequestParam("phone") String phone) {
        return processFilesService.queueFile(file, firstName, lastName, email, phone);
    }

    @PostMapping("/process/status")
    public StatusResponseDto processStatus(@RequestBody StatusRequestDto requestDto) {
        return processFilesService.processStatus(requestDto);
    }
}
