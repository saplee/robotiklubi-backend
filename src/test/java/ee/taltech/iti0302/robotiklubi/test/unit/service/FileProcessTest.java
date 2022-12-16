package ee.taltech.iti0302.robotiklubi.test.unit.service;


import ee.taltech.iti0302.robotiklubi.mappers.client.ClientMapper;
import ee.taltech.iti0302.robotiklubi.mappers.client.ClientMapperImpl;
import ee.taltech.iti0302.robotiklubi.mappers.order.OrderMapper;
import ee.taltech.iti0302.robotiklubi.mappers.order.OrderMapperImpl;
import ee.taltech.iti0302.robotiklubi.repository.ClientRepository;
import ee.taltech.iti0302.robotiklubi.repository.OrderRepository;
import ee.taltech.iti0302.robotiklubi.service.ProcessFilesService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileProcessTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Spy
    private OrderMapper orderMapper = new OrderMapperImpl();
    @Spy
    private ClientMapper clientMapper = new ClientMapperImpl();


    @InjectMocks
    private ProcessFilesService processFilesService;



}
