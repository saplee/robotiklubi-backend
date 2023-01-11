package ee.taltech.iti0302.robotiklubi.test.unit.service;

import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;


import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapper;
import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapperImpl;
import ee.taltech.iti0302.robotiklubi.repository.*;

import ee.taltech.iti0302.robotiklubi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private final UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private UserService userService;

    @Test
    void getAllRegularMemberTest() {
        User user = User.builder().firstName("First name").id(2L).role(2).email("test@test").isAdmin(false).lastName("Last name").password("123").phone("123").build();
        UserDto userDto = UserDto.builder().firstName("First name").email("test@test").lastName("Last name").phone("123").build();
        // given
        given(userRepository.findAllByRole(2)).willReturn(new ArrayList<>(List.of(user)));
        // then
        List<UserDto> result = new ArrayList<>(List.of(userDto));
        List<UserDto> actual = userService.getAllRegularMembers();
        then(userMapper).should().toDto(user);
        then(userMapper).should().toDtoList(new ArrayList<>(List.of(user)));
        then(userRepository).should().findAllByRole(2);
        assertEquals(result.get(0), actual.get(0));
    }


    @Test
    void getAllManagementTest() {
        User user = User.builder().firstName("First name").id(2L).role(4).email("test@test").isAdmin(false).lastName("Last name").password("123").phone("123").build();
        UserDto userDto = UserDto.builder().firstName("First name").email("test@test").lastName("Last name").phone("123").build();
        // given
        given(userRepository.findAllByRole(4)).willReturn(new ArrayList<>(List.of(user)));
        // then
        List<UserDto> result = new ArrayList<>(List.of(userDto));
        List<UserDto> actual = userService.getAllManagement();
        then(userMapper).should().toDto(user);
        then(userMapper).should().toDtoList(new ArrayList<>(List.of(user)));
        then(userRepository).should().findAllByRole(4);
        assertEquals(result.get(0), actual.get(0));
    }
}