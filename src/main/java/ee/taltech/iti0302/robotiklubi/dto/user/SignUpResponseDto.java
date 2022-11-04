package ee.taltech.iti0302.robotiklubi.dto.user;

import lombok.Data;

@Data
public class SignUpResponseDto {
    boolean succeeded = false;
    boolean emailError = false;
}
