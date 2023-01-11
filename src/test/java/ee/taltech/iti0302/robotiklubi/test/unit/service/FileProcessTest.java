package ee.taltech.iti0302.robotiklubi.test.unit.service;


import ee.taltech.iti0302.robotiklubi.dto.order.StatusRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusResponseDto;
import ee.taltech.iti0302.robotiklubi.exception.FileProcessingException;
import ee.taltech.iti0302.robotiklubi.mappers.order.OrderMapper;
import ee.taltech.iti0302.robotiklubi.mappers.order.OrderMapperImpl;
import ee.taltech.iti0302.robotiklubi.repository.Client;
import ee.taltech.iti0302.robotiklubi.repository.ClientRepository;
import ee.taltech.iti0302.robotiklubi.repository.Order;
import ee.taltech.iti0302.robotiklubi.repository.OrderRepository;
import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FileProcessTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ProcessFilesService processFilesService;

    // This is needed for the processStatus method
    @Spy
    private OrderMapper orderMapper = new OrderMapperImpl();

    @Test
    void gcodeFileName() {
        MultipartFile file = new MockMultipartFile("file", "filename.stl", "application/sla", "some stl".getBytes());
        //when
        String result = processFilesService.createGcodeFilename(file.getOriginalFilename());
        //then
        assertEquals("filename.gcode", result);
    }

    @Test
    void processFiles() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.stl", "application/sla", "some stl".getBytes());
        // given
        Order order = Order.builder()
                .fileStl(file.getBytes())
                .sliced(false)
                .printed(false)
                .fileName(file.getOriginalFilename())
                .build();
        given(orderRepository.findAllBySliced(false)).willReturn(new ArrayList<>(List.of(order)));
        //when
        try {
            processFilesService.processFiles();
            fail();
        } catch (FileProcessingException ignored) {}

    }

    @Test
    void processStatusPending() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.stl", "application/sla", "some stl".getBytes());
        String firstName = "John1";
        String lastName = "Doe1";
        String email = "john.doe@mail.com";
        String phone = "123456789";
        // given
        Order order = Order.builder()
                .fileStl(file.getBytes())
                .sliced(false)
                .printed(false)
                .fileName(file.getOriginalFilename())
                .build();
        given(clientRepository.findAllByFirstNameAndLastNameAndEmailAndPhoneNumber(
                firstName, lastName, email, phone)).willReturn(new ArrayList<>(List.of(Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phone)
                .build())));
        given(orderRepository.findAllByClient(Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phone)
                .build())).willReturn(new ArrayList<>(List.of(order)));
        StatusRequestDto statusRequestDto = StatusRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .fileName(file.getOriginalFilename())
                .build();
        //when
        StatusResponseDto result = processFilesService.processStatus(statusRequestDto);
        //then
        assertEquals("pending", result.getStatus());

    }

    @Test
    void processStatusDone() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.stl", "application/sla", "some stl".getBytes());
        String firstName = "John1";
        String lastName = "Doe1";
        String email = "john.doe@mail.com";
        String phone = "123456789";
        // given
        Order order = Order.builder()
                .fileStl(file.getBytes())
                .sliced(true)
                .printed(false)
                .fileName(file.getOriginalFilename())
                .build();
        given(clientRepository.findAllByFirstNameAndLastNameAndEmailAndPhoneNumber(
                firstName, lastName, email, phone)).willReturn(new ArrayList<>(List.of(Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phone)
                .build())));
        given(orderRepository.findAllByClient(Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phone)
                .build())).willReturn(new ArrayList<>(List.of(order)));
        StatusRequestDto statusRequestDto = StatusRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .fileName(file.getOriginalFilename())
                .build();
        //when
        StatusResponseDto result = processFilesService.processStatus(statusRequestDto);
        //then
        assertEquals("done", result.getStatus());

    }


    @Test
    void getGcodeData() throws IOException {
        InputStream inputStream = new FileInputStream(
                "src/test/java/ee/taltech/iti0302/robotiklubi/test/unit/service/FileProcessInputStream.txt");
        Map<ProcessFilesService.GcodeValues, Float> results = processFilesService.getGcodeData(inputStream);
        assertEquals(16668f, results.get(ProcessFilesService.GcodeValues.MATERIAL_USED));
        assertEquals(11152f, results.get(ProcessFilesService.GcodeValues.PRINT_TIME));
        assertEquals(257f, results.get(ProcessFilesService.GcodeValues.LAYER_COUNT));
        assertEquals(0.12f, results.get(ProcessFilesService.GcodeValues.LAYER_HEIGHT));
    }


}
