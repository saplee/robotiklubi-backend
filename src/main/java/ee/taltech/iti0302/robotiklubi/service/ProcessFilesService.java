package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.order.OrderResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusRequestDto;
import ee.taltech.iti0302.robotiklubi.dto.order.StatusResponseDto;
import ee.taltech.iti0302.robotiklubi.exception.FileProcessingException;
import ee.taltech.iti0302.robotiklubi.mappers.order.OrderMapper;
import ee.taltech.iti0302.robotiklubi.repository.Client;
import ee.taltech.iti0302.robotiklubi.repository.ClientRepository;
import ee.taltech.iti0302.robotiklubi.repository.Order;
import ee.taltech.iti0302.robotiklubi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProcessFilesService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto processFiles(MultipartFile file, String firstName, String lastName, String email,
                                         String phone) {
        try {
            String uploadsFolder = "/uploads/";
            String fileName = file.getOriginalFilename();
            if (fileName == null) return new OrderResponseDto(false);

            fileName = fileName.replace(" ", "_");
            String gcodeFileName = fileName.substring(0, fileName.length() - 4) + ".gcode";

            Path basePath = Paths.get("");
            File localFile = new File(basePath.toAbsolutePath() + uploadsFolder + fileName);
            file.transferTo(localFile);

            Long id = clientRepository.save(Client.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phoneNumber(phone)
                    .build()).getId();
            Order order = Order.builder()
                    .clientId(id)
                    .fileName(gcodeFileName)
                    .fileStl(file.getBytes())
                    .sliced(false)
                    .printed(false)
                    .build();
            Long orderId = orderRepository.save(order).getId();

            Process pr = new ProcessBuilder("curaengine", "slice", "-j", "/opt/PrinterConfigs/RobotiklubiConf.def.json",
                    "-l",
                    uploadsFolder + fileName, "-o", uploadsFolder + gcodeFileName).start();

            File gcodeFile = new File(basePath.toAbsolutePath() + uploadsFolder + gcodeFileName);

            Map<GcodeValues, Float> data = getGcodeData(pr);

            orderRepository.findById(orderId).ifPresent(o -> {
                o.setSliced(true);
                try {
                    o.setFileGcode(Files.readAllBytes(gcodeFile.toPath()));
                } catch (IOException e) {
                    throw new FileProcessingException("Error while reading gcode file");
                }
                o.setLayerCount(data.get(GcodeValues.LAYER_COUNT).intValue());
                o.setLayerHeight(data.get(GcodeValues.LAYER_HEIGHT));
                o.setMaterialUsed(data.get(GcodeValues.MATERIAL_USED));
                o.setPrintTime(data.get(GcodeValues.PRINT_TIME).intValue());
                o.setPrice(calculatePrice(o));
                orderRepository.save(o);
            });

            return new OrderResponseDto(true);
        } catch (IOException e) {
            throw new FileProcessingException("File processing failed");
        }
    }

    private Float calculatePrice(Order order) {
        return order.getLayerCount() * order.getLayerHeight() * order.getMaterialUsed();
    }

    @Transactional()
    public StatusResponseDto processStatus(StatusRequestDto requestDto) {
        List<Client> clients = clientRepository.findAllByFirstNameAndLastNameAndEmailAndPhoneNumber(
                requestDto.getFirstName(), requestDto.getLastName(), requestDto.getEmail(), requestDto.getPhone());
        log.info("{}", clients);
        for (Client client : clients) {
            log.info("Client: {}", client);
            List<Order> orders = orderRepository.findAllByClientId(client.getId());
            log.info("{}", orders);
            for (Order order : orders) {
                if (order.getFileName()
                        .strip()
                        .substring(0, order.getFileName().lastIndexOf("."))
                        .equals(requestDto.getFileName().substring(0, requestDto.getFileName().lastIndexOf("."))) &&
                        Boolean.TRUE.equals(order.getSliced())) {
                    return new StatusResponseDto("done", orderMapper.toDto(order));
                }
            }
        }
        return new StatusResponseDto("pending");
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
        LAYER_HEIGHT
    }

}
