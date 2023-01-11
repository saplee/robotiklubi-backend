package ee.taltech.iti0302.robotiklubi.dto.order;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusRequestDto {
    private String fileName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
