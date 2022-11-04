package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.user.SignUpResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;
import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapper;
import ee.taltech.iti0302.robotiklubi.repository.User;
import ee.taltech.iti0302.robotiklubi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserDto> getAllRegularMembers() {
        return userMapper.toDtoList(userRepository.findAllByRole(2));
    }
    @Transactional(readOnly = true)
    public List<UserDto> getAllManagement() {
        return userMapper.toDtoList(userRepository.findAllByRole(4));
    }

    public SignUpResponseDto addUser(User user) {
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        user.setRole(1);
        user.setIsAdmin(false);
        user.setEmail(user.getEmail().toLowerCase());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            signUpResponseDto.setSucceeded(false);
            signUpResponseDto.setEmailError(true);
        } else {
            try {
                userRepository.save(user);
                signUpResponseDto.setSucceeded(true);
                signUpResponseDto.setEmailError(false);
            } catch (Exception exception) {
                signUpResponseDto.setSucceeded(false);
                signUpResponseDto.setEmailError(false);
            }
        }
        return signUpResponseDto;
    }
}
