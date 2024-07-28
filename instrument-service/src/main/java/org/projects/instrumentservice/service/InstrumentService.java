package org.projects.instrumentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.instrumentservice.dto.InstrumentCreateDto;
import org.projects.instrumentservice.dto.InstrumentResponseDto;
import org.projects.instrumentservice.exception.ResourceNotFoundException;
import org.projects.instrumentservice.mapper.InstrumentCreateDtoMapper;
import org.projects.instrumentservice.mapper.InstrumentResponseDtoMapper;
import org.projects.instrumentservice.model.Instrument;
import org.projects.instrumentservice.repository.InstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentCreateDtoMapper instrumentCreateMapper;
    private final InstrumentResponseDtoMapper instrumentResponseMapper;

    public void createInstrument(InstrumentCreateDto instrumentDto) {
        Instrument instrument = instrumentCreateMapper.toEntity(instrumentDto);
        instrumentRepository.save(instrument);
        log.info("Instrument {} is created", instrument.getId());
    }

    public List<InstrumentResponseDto> getAllInstruments() {
        return instrumentRepository.findAll().stream()
                .map(instrumentResponseMapper::toDto)
                .toList();
    }

    public InstrumentResponseDto getInstrumentById(String id) {
        return instrumentRepository.findById(id)
                .map(instrumentResponseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument with id " + id + " not found"));
    }
}
