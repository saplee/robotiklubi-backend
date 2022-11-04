package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;
import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapper;
import ee.taltech.iti0302.robotiklubi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllRegularMembers() {
        return userMapper.toDtoList(userRepository.findAllByRole(2));
    }

    public List<UserDto> getAllManagement() {
        return userMapper.toDtoList(userRepository.findAllByRole(4));
    }
}
