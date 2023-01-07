package ee.taltech.iti0302.robotiklubi.test.unit.mappers;

import ee.taltech.iti0302.robotiklubi.dto.order.OrderDto;
import ee.taltech.iti0302.robotiklubi.mappers.order.OrderMapper;
import ee.taltech.iti0302.robotiklubi.repository.Order;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void orderToDtoTest() {
        Order order = Order.builder()
                .layerCount(15)
                .layerHeight(20f)
                .printTime(8)
                .fileName("file.stl")
                .materialUsed(30f)
                .price(40f)
                .build();
        OrderDto dto = mapper.toDto(order);
        assertEquals(15, dto.getLayerCount());
        assertEquals(20d, dto.getLayerHeight());
        assertEquals(8, dto.getPrintTime());
        assertEquals("file.stl", dto.getFileName());
        assertEquals(30d, dto.getMaterialUsed());
        assertEquals(40d, dto.getPrice());
    }

    @Test
    void orderToDtoNullTest() {
        OrderDto dto = mapper.toDto(null);
        assertNull(dto);
    }

}
