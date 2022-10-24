package ee.taltech.iti0302.robotiklubi.service;

import com.sun.istack.NotNull;
import ee.taltech.iti0302.robotiklubi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService {

    @NotNull
    private final ClientRepository clientRepository;
}
