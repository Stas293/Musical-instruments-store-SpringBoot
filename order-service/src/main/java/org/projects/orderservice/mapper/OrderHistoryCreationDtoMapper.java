package org.projects.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.projects.orderservice.dto.OrderHistoryCreationDto;
import org.projects.orderservice.model.Order;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface OrderHistoryCreationDtoMapper {
    @Mapping(target = "status", source = "status.name")
    OrderHistoryCreationDto toDto(Order order);
}
