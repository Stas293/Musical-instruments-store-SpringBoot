package org.projects.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.projects.orderservice.dto.StatusResponseDto;
import org.projects.orderservice.model.Status;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusResponseDtoMapper {
    StatusResponseDto toDto(Status status);
}
