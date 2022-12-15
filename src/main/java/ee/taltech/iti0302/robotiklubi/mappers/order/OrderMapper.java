package ee.taltech.iti0302.robotiklubi.mappers.order;

import ee.taltech.iti0302.robotiklubi.dto.order.OrderDto;
import ee.taltech.iti0302.robotiklubi.repository.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderDto toDto(Order order);

}
