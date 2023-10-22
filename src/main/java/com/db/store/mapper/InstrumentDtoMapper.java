package com.db.store.mapper;

import com.db.store.dto.InstrumentDTO;
import com.db.store.model.Instrument;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InstrumentDtoMapper {
    Instrument toEntity(InstrumentDTO instrumentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Instrument instrument, @MappingTarget Instrument instrumentToUpdate);

    InstrumentDTO toDto(Instrument instrument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Instrument partialUpdate(InstrumentDTO instrumentDTO, @MappingTarget Instrument instrument);
}