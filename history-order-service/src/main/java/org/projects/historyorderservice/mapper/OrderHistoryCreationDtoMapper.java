package org.projects.historyorderservice.mapper;

import org.mapstruct.*;
import org.projects.historyorderservice.dto.OrderHistoryCreationDto;
import org.projects.historyorderservice.model.OrderHistory;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderHistoryCreationDtoMapper {
    OrderHistory toEntity(OrderHistoryCreationDto orderDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(OrderHistory order, @MappingTarget OrderHistory orderToUpdate);

    OrderHistoryCreationDto toDto(OrderHistory order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderHistory partialUpdate(OrderHistoryCreationDto orderDto, @MappingTarget OrderHistory order);
}
