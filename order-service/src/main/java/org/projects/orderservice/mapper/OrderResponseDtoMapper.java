package org.projects.orderservice.mapper;

import org.mapstruct.*;
import org.projects.orderservice.dto.OrderResponseDto;
import org.projects.orderservice.model.Order;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderResponseDtoMapper {
    @Mapping(target = "status", source = "status.name")
    OrderResponseDto toDto(Order order);
}
