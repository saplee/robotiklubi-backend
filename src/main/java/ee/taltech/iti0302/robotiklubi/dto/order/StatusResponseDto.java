package ee.taltech.iti0302.robotiklubi.dto.order;

import lombok.Data;

@Data
public class StatusResponseDto {

    private String status;

    public StatusResponseDto(String status) {
        this.status = status;
    }
}
