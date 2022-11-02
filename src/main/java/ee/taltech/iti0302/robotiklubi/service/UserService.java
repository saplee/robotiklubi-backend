package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.user.SignUpResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;
import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapper;
import ee.taltech.iti0302.robotiklubi.repository.User;
import ee.taltech.iti0302.robotiklubi.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final UserMapper userMapper;

    public List<UserDto> getAllRegularMembers() {
        return userMapper.toDtoList(userRepository.findAllByRole(2));
    }

    public List<UserDto> getAllManagement() {
        return userMapper.toDtoList(userRepository.findAllByRole(4));
    }

    public SignUpResponseDto addUser(User user) {
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        userRepository.save(user);
        signUpResponseDto.setSucceeded(true);
        return signUpResponseDto;
    }
}
