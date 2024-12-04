package org.projects.historyorderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.projects.historyorderservice.dto.OrderHistoryResponseDto;
import org.projects.historyorderservice.model.OrderHistory;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderHistoryResponseDtoMapper {
    OrderHistoryResponseDto toDto(OrderHistory order);
}
