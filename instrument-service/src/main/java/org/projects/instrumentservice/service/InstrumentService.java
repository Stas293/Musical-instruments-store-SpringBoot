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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentCreateDtoMapper instrumentCreateMapper;
    private final InstrumentResponseDtoMapper instrumentResponseMapper;
    private final WebClient.Builder webClientBuilder;

    public void createInstrument(InstrumentCreateDto instrumentDto) {
        Instrument instrument = instrumentCreateMapper.toEntity(instrumentDto);
        instrumentRepository.save(instrument);
        log.info("Instrument {} is created", instrument.getId());
    }

    public Page<InstrumentResponseDto> getInstrumentsPage(int page, int size) {
        return instrumentRepository.findAll(PageRequest.of(page, size))
                .map(instrumentResponseMapper::toDto);
    }

    public InstrumentResponseDto getInstrumentById(String id) {
        return instrumentRepository.findById(id)
                .map(this::fetchAvailability)
                .map(instrumentResponseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument with id " + id + " not found"));
    }

    private Instrument fetchAvailability(Instrument instrument) {
        Integer inventory = webClientBuilder.build()
                .get()
                .uri(String.format("http://inventory-service/api/inventory/%s", instrument.getId()))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        instrument.setQuantity(inventory);
        return instrument;
    }
}
