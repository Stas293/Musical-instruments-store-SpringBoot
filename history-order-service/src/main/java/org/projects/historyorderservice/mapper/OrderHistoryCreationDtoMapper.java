package org.projects.historyorderservice.mapper;

import org.mapstruct.*;
import org.projects.historyorderservice.dto.OrderHistoryCreationDto;
import org.projects.historyorderservice.model.OrderHistory;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderHistoryCreationDtoMapper {
    @Mapping(target = "status.name", source = "status")
    OrderHistory toEntity(OrderHistoryCreationDto orderDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(OrderHistory order, @MappingTarget OrderHistory orderToUpdate);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status.name", source = "status")
    OrderHistory partialUpdate(OrderHistoryCreationDto orderDto, @MappingTarget OrderHistory order);
}
