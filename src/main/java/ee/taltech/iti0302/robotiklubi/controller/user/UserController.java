package ee.taltech.iti0302.robotiklubi.controller.user;

import ee.taltech.iti0302.robotiklubi.dto.user.*;
import ee.taltech.iti0302.robotiklubi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/users/data")
    public UserDetailedDto getUserData(Principal principal) {
        return userService.getUserInfo(principal.getName());
    }

    @GetMapping("/users/members")
    public List<UserDto> getAllRegularClubMembers() {
        return userService.getAllRegularMembers();
    }

    @GetMapping("/users/management")
    public List<UserDto> getAllManagementMembers() {
        return userService.getAllManagement();
    }

    @PostMapping("/user/signup")
    public SignUpResponseDto registerUser(@RequestBody SignUpUserDto user) {
        return userService.addUser(user);
    }

    @PostMapping("/user/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return userService.login(request);
    }

    @PostMapping("/user/refresh")
    public RefreshResponseDto refresh(@RequestBody RefreshRequestDto request) {
        return userService.refresh(request);
    }

    @PutMapping("/user/update")
    public void updateUser(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
    }

}
