package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.user.*;
import ee.taltech.iti0302.robotiklubi.exception.InternalServerException;
import ee.taltech.iti0302.robotiklubi.exception.NotFoundException;
import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapper;
import ee.taltech.iti0302.robotiklubi.repository.User;
import ee.taltech.iti0302.robotiklubi.repository.UserRepository;
import ee.taltech.iti0302.robotiklubi.tokens.TokenBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final long TOKEN_EXPIRATION_TIME_S = 60L * 60L;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME_S = 60L * 60L * 24L;
    private final PasswordEncoder passwordEncoder;

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
        SignUpResponseDto signUpResponseDto = SignUpResponseDto.builder().build();
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            signUpResponseDto.setSucceeded(false);
            signUpResponseDto.setEmailError(true);
        } else {
            try {
                User tempUser = User.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .phone(user.getPhone())
                        .role(1)
                        .isAdmin(false)
                        .build();
                userRepository.save(tempUser);
                signUpResponseDto.setSucceeded(true);
                signUpResponseDto.setEmailError(false);
            } catch (Exception exception) {
                signUpResponseDto.setSucceeded(false);
                signUpResponseDto.setEmailError(false);
            }
        }
        return signUpResponseDto;
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        LoginResponseDto loginResponse = LoginResponseDto.builder().build();
        Optional<User> user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail());
        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            loginResponse.setSucceeded(true);
            loginResponse.setAccessToken(TokenBuilder.createToken(
                    user.get().getId(),
                    user.get().getRole(),
                    TOKEN_EXPIRATION_TIME_S));
            loginResponse.setRefreshToken(TokenBuilder.createToken(
                    user.get().getId(),
                    user.get().getRole(),
                    REFRESH_TOKEN_EXPIRATION_TIME_S));

        }
        return loginResponse;
    }

    @Transactional(readOnly = true)
    public RefreshResponseDto refresh(RefreshRequestDto refreshRequest) {
        RefreshResponseDto refreshResponse = RefreshResponseDto.builder().build();
        refreshResponse.setAccessToken(TokenBuilder.createToken(
                TokenBuilder.fromToken(refreshRequest.getAccessToken()).get("id", Long.class),
                TokenBuilder.fromToken(refreshRequest.getAccessToken()).get("auth", Integer.class),
                TOKEN_EXPIRATION_TIME_S));
        refreshResponse.setRefreshToken(TokenBuilder.createToken(
                TokenBuilder.fromToken(refreshRequest.getAccessToken()).get("id", Long.class),
                TokenBuilder.fromToken(refreshRequest.getAccessToken()).get("auth", Integer.class),
                REFRESH_TOKEN_EXPIRATION_TIME_S));
        refreshResponse.setSucceeded(true);
        return refreshResponse;
    }

    public UserDetailedDto getUserInfo(String idString) {
        Long id = Long.valueOf(idString);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("User not found.");
        return userMapper.toDetailedDto(user.get());
    }

    public void updateUser(Long id, UserDto userDto) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) throw new NotFoundException("User not found.");
            User user = userOptional.get();
            user.setPhone(userDto.getPhone());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            userRepository.save(user);
        } catch (Exception e) {throw new InternalServerException("Could not update user (id " + id + ").", e);}
    }

}
