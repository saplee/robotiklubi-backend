package ee.taltech.iti0302.robotiklubi.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
    private Integer layerCount;
    private Double layerHeight;
    private Integer printTime;
    private String fileName;
    private Double materialUsed;
}
