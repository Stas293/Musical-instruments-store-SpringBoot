package com.db.store.mapper;

import com.db.store.dto.StatusDTO;
import com.db.store.model.Status;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusDtoMapper {
    Status toEntity(StatusDTO statusDTO);

    StatusDTO toDto(Status status);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Status partialUpdate(StatusDTO statusDTO, @MappingTarget Status status);
}