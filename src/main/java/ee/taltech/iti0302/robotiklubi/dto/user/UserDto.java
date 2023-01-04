package ee.taltech.iti0302.robotiklubi.dto.user;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
