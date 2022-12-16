package ee.taltech.iti0302.robotiklubi.dto.order;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OrderRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private MultipartFile file;
}
