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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProcessFilesService {

    private static final int MAX_AMOUNT_TO_PROCESS = 100;
    private static final Float PLA_DENSITY = 0.00124f; // g/mm^3

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;


    public String createGcodeFilename(String stlFilename) {
        stlFilename = stlFilename.replace(" ", "_");
        return stlFilename.substring(0, stlFilename.length() - 4) + ".gcode";
    }


    @Transactional
    public OrderResponseDto queueFile(MultipartFile file, String firstName, String lastName, String email,
                                      String phone) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.substring(fileName.lastIndexOf(".")).equals(".stl")) {
                return new OrderResponseDto(false);
            }
            String gcodeFileName = createGcodeFilename(fileName);
            Client client = Client.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phoneNumber(phone)
                    .build();
            clientRepository.save(client);
            Order order = Order.builder()
                    .client(client)
                    .fileName(gcodeFileName)
                    .fileStl(file.getBytes())
                    .sliced(false)
                    .printed(false)
                    .build();
            orderRepository.save(order);
            return new OrderResponseDto(true);
        } catch (IOException e) {
            throw new FileProcessingException("Error while queueing file");
        }

    }

    @Transactional
    public void processFiles() {
        List<Order> orders = orderRepository.findAllBySliced(false);
        orders.sort(Comparator.comparing(Order::getId));
        orders = orders.stream().limit(MAX_AMOUNT_TO_PROCESS).toList();
        for (Order order : orders) {
            try {
                log.info("Processing file {}", order.getFileName());
                String processingFolder = "/processing/";
                String stlFileName = order.getFileName().substring(0, order.getFileName().lastIndexOf(".")) + ".stl";

                Path basePath = Paths.get("");
                File localStlFile = new File(basePath.toAbsolutePath() + processingFolder + stlFileName);
                localStlFile.getParentFile().mkdirs();

                writeDataToFile(localStlFile, order.getFileStl());
                String gcodeFileName = order.getFileName();
                Process pr =
                        new ProcessBuilder("curaengine", "slice", "-j", "/opt/PrinterConfigs/RobotiklubiConf.def.json",
                                "-l", processingFolder + stlFileName,
                                "-o", processingFolder + gcodeFileName).start();
                pr.waitFor();
                Map<GcodeValues, Float> data = getGcodeData(pr.getInputStream());
                File gcodeFile = new File(basePath.toAbsolutePath() + processingFolder + gcodeFileName);
                if (!Files.deleteIfExists(localStlFile.toPath())) {
                    throw new FileProcessingException(
                            "STL File could not be found for deletion " + localStlFile.toPath());
                }
                orderRepository.findById(order.getId()).ifPresent(o -> {
                    o.setSliced(true);
                    try {
                        o.setFileGcode(Files.readAllBytes(gcodeFile.toPath()));
                    } catch (IOException e) {
                        throw new FileProcessingException("Error while reading gcode file");
                    }
                    o.setLayerCount(data.getOrDefault(GcodeValues.LAYER_COUNT, 0f).intValue());
                    o.setLayerHeight(data.getOrDefault(GcodeValues.LAYER_HEIGHT, 0f));
                    o.setMaterialUsed(data.getOrDefault(GcodeValues.MATERIAL_USED, 0f));
                    o.setPrintTime(data.getOrDefault(GcodeValues.PRINT_TIME, 0f).intValue());
                    o.setPrice(calculatePrice(o));
                    orderRepository.save(o);
                });
            } catch (IOException e) {
                throw new FileProcessingException("File processing failed");
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void writeDataToFile(File file, byte[] data) {
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(data);
        } catch (IOException e) {
            throw new FileProcessingException("Error while writing data to file");
        }
    }

    private Float calculatePrice(Order order) {
        return 3 + order.getMaterialUsed() * PLA_DENSITY * 0.1f + (int) (order.getPrintTime() / 3600D) * 1.5f;
    }

    @Transactional(readOnly = true)
    public StatusResponseDto processStatus(StatusRequestDto requestDto) {
        List<Client> clients = clientRepository.findAllByFirstNameAndLastNameAndEmailAndPhoneNumber(
                requestDto.getFirstName(), requestDto.getLastName(), requestDto.getEmail(), requestDto.getPhone());
        for (Client client : clients) {
            List<Order> orders = orderRepository.findAllByClient(client);
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

    public Map<GcodeValues, Float> getGcodeData(InputStream inputStream) throws IOException {
        BufferedReader bfr = new BufferedReader(new java.io.InputStreamReader(inputStream));
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

    public enum GcodeValues {
        MATERIAL_USED,
        PRINT_TIME,
        LAYER_COUNT,
        LAYER_HEIGHT
    }

}
