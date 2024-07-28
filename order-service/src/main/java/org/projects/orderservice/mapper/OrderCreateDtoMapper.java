package org.projects.orderservice.mapper;

import org.mapstruct.*;
import org.projects.orderservice.dto.OrderCreationDto;
import org.projects.orderservice.model.Order;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderCreateDtoMapper {
    Order toEntity(OrderCreationDto orderDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Order order, @MappingTarget Order orderToUpdate);

    OrderCreationDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderCreationDto orderDto, @MappingTarget Order order);
}
