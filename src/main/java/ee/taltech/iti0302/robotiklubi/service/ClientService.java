package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
}
