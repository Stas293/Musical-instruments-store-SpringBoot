package org.projects.instrumentservice.mapper;

import org.mapstruct.*;
import org.projects.instrumentservice.dto.InstrumentResponseDto;
import org.projects.instrumentservice.model.Instrument;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface InstrumentResponseDtoMapper {
    Instrument toEntity(InstrumentResponseDto instrumentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Instrument instrument, @MappingTarget Instrument instrumentToUpdate);

    InstrumentResponseDto toDto(Instrument instrument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Instrument partialUpdate(InstrumentResponseDto instrumentDTO, @MappingTarget Instrument instrument);
}