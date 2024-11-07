package org.projects.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.projects.orderservice.dto.InstrumentOrderDataResponseDto;
import org.projects.orderservice.model.InstrumentOrder;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InstrumentOrderResponseDtoMapper {
    InstrumentOrderDataResponseDto toDto(InstrumentOrder instrumentOrder);

    List<InstrumentOrderDataResponseDto> toDto(List<InstrumentOrder> instrumentOrders);
}
