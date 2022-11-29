package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.user.SignUpResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.user.SignUpUserDto;
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
    @Transactional
    public SignUpResponseDto addUser(SignUpUserDto user) {
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            signUpResponseDto.setSucceeded(false);
            signUpResponseDto.setEmailError(true);
        } else {
            try {
                User user1 = User.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword()).phone(user.getPhone()).role(1).isAdmin(false).build();
                userRepository.save(user1);
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
