package ee.taltech.iti0302.robotiklubi.test.unit.mappers;

import ee.taltech.iti0302.robotiklubi.dto.user.UserDetailedDto;
import ee.taltech.iti0302.robotiklubi.dto.user.UserDto;
import ee.taltech.iti0302.robotiklubi.mappers.user.UserMapper;
import ee.taltech.iti0302.robotiklubi.repository.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userToDtoTest() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("a@b.c")
                .phone("+1234567")
                .build();
        UserDto dto = mapper.toDto(user);
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("a@b.c", dto.getEmail());
        assertEquals("+1234567", dto.getPhone());
    }

    @Test
    void userToDtoNullTest() {
        UserDto dto = mapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void userToDetailedDtoTest() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("a@b.c")
                .phone("+1234567")
                .role(5)
                .isAdmin(true)
                .build();
        UserDetailedDto dto = mapper.toDetailedDto(user);
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("a@b.c", dto.getEmail());
        assertEquals("+1234567", dto.getPhone());
        assertEquals(5, dto.getRole());
        assertEquals(true, dto.getIsAdmin());
    }

    @Test
    void userToDetailedDtoNullTest() {
        UserDetailedDto dto = mapper.toDetailedDto(null);
        assertNull(dto);
    }

    @Test
    void userToDtoListNullTest() {
        List<UserDto> dto = mapper.toDtoList(null);
        assertNull(dto);
    }
}
