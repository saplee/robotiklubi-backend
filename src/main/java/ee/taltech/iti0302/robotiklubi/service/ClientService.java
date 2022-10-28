package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.repository.ClientRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService {

    @NonNull
    private final ClientRepository clientRepository;
}
