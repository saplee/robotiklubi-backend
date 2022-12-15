package ee.taltech.iti0302.robotiklubi.dto.order;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StatusResponseDto {

    private final String status;
    private Integer layerCount;
    private Double layerHeight;
    private Integer printTime;
    private String fileName;
    private Double materialUsed;

}
