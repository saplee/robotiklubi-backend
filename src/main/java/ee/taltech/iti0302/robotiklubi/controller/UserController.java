package ee.taltech.iti0302.robotiklubi.controller;

import ee.taltech.iti0302.robotiklubi.dto.user.SignUpResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;
import ee.taltech.iti0302.robotiklubi.repository.User;
import ee.taltech.iti0302.robotiklubi.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    @NonNull
    private UserService userService;

    @GetMapping("/users/members")
    public List<UserDto> getAllRegularClubMembers() {
        return userService.getAllRegularMembers();
    }

    @GetMapping("/users/management")
    public List<UserDto> getAllManagementMembers() {
        return userService.getAllManagement();
    }

    @PostMapping("/signup")
    public SignUpResponseDto registerUser(@RequestBody User user) {
        return userService.addUser(user);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
