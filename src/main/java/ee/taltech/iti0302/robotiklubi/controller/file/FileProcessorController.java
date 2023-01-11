package ee.taltech.iti0302.robotiklubi.controller.file;

import ee.taltech.iti0302.robotiklubi.dto.order.OrderRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.order.OrderResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusResponseDto;
import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FileProcessorController {

    private final ProcessFilesService processFilesService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            value = "/process")
    public OrderResponseDto processFiles(OrderRequestDto orderRequestDto) {
        return processFilesService.queueFile(orderRequestDto.getFile(), orderRequestDto.getFirstName(),
                orderRequestDto.getLastName(), orderRequestDto.getEmail(), orderRequestDto.getPhone());
    }

    @PostMapping("/process/status")
    public StatusResponseDto processStatus(@RequestBody StatusRequestDto requestDto) {
        return processFilesService.processStatus(requestDto);
    }
}
