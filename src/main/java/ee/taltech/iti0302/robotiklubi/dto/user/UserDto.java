package ee.taltech.iti0302.robotiklubi.dto.user;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
