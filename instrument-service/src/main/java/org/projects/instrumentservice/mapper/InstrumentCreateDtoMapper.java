package org.projects.instrumentservice.mapper;

import org.mapstruct.*;
import org.projects.instrumentservice.dto.InstrumentCreateDto;
import org.projects.instrumentservice.model.Instrument;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InstrumentCreateDtoMapper {
    Instrument toEntity(InstrumentCreateDto instrumentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Instrument instrument, @MappingTarget Instrument instrumentToUpdate);

    InstrumentCreateDto toDto(Instrument instrument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Instrument partialUpdate(InstrumentCreateDto instrumentDTO, @MappingTarget Instrument instrument);
}
