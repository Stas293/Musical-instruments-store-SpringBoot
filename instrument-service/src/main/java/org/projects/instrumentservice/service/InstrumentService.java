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
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentCreateDtoMapper instrumentCreateMapper;
    private final InstrumentResponseDtoMapper instrumentResponseMapper;
    private final Environment environment;
    private final WebClient.Builder webClientBuilder;

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public void createInstrument(InstrumentCreateDto instrumentDto) {
        Instrument instrument = instrumentCreateMapper.toEntity(instrumentDto);
        instrumentRepository.save(instrument);
        log.info("Instrument {} is created", instrument.getId());
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    public Page<InstrumentResponseDto> getInstrumentsPage(int page, int size) {
        return instrumentRepository.findAll(PageRequest.of(page, size))
                .map(instrumentResponseMapper::toDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
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
                .header("Authorization",
                        "Bearer %s%s".formatted(
                                environment.getProperty("api.key.inventory"),
                                environment.getProperty("application.jwt.secret")))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        instrument.setQuantity(inventory);
        return instrument;
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'SERVICE')")
    public List<InstrumentResponseDto> getInstrumentsByIds(List<String> instrumentIds) {
        return instrumentRepository.findByIdIn(instrumentIds)
                .stream()
                .map(instrumentResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
