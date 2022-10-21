package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
}
