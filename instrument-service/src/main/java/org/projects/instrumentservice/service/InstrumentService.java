package org.projects.instrumentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.instrumentservice.client.InventoryClient;
import org.projects.instrumentservice.constants.InstrumentConstants;
import org.projects.instrumentservice.dto.InstrumentCreateDto;
import org.projects.instrumentservice.dto.InstrumentResponseDto;
import org.projects.instrumentservice.exception.InstrumentConflictException;
import org.projects.instrumentservice.exception.ResourceNotFoundException;
import org.projects.instrumentservice.exception.ValidationException;
import org.projects.instrumentservice.mapper.InstrumentCreateDtoMapper;
import org.projects.instrumentservice.mapper.InstrumentResponseDtoMapper;
import org.projects.instrumentservice.model.Instrument;
import org.projects.instrumentservice.repository.InstrumentRepository;
import org.projects.instrumentservice.validation.InstrumentValidator;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentCreateDtoMapper instrumentCreateMapper;
    private final InstrumentResponseDtoMapper instrumentResponseMapper;
    private final InstrumentValidator instrumentValidator;
    private final Environment environment;
    private final InventoryClient inventoryClient;

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public String createInstrument(InstrumentCreateDto instrumentDto, BindingResult bindingResult) {
        instrumentValidator.validate(instrumentDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.error("Instrument validation failed: {}", bindingResult.getFieldErrors());
            throw new ValidationException(bindingResult.getFieldErrors().toString());
        }
        Instrument instrument = instrumentCreateMapper.toEntity(instrumentDto);
        try {
            String id = instrumentRepository.save(instrument).getId();
            setAvailability(instrument);
            log.info("Instrument {} is created", instrument.getId());
            return id;
        } catch (DataAccessException e) {
            log.error("Failed to create instrument: {}", e.getMessage());
            throw new RuntimeException("Failed to create instrument");
        } catch (WebClientResponseException e) {
            log.error("Failed to set availability: {}", e.getMessage());
            instrumentRepository.delete(instrument);
            throw new RuntimeException("Failed to set availability");
        }
    }

    private void setAvailability(Instrument instrument) {
        inventoryClient.setInventory("Bearer %s%s".formatted(
                        environment.getProperty("api.key.inventory"),
                        environment.getProperty("application.jwt.secret")),
                instrument.getId(),
                instrument.getQuantity());
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
        Integer inventory = inventoryClient.getInventory(
                "Bearer %s%s".formatted(
                        environment.getProperty("api.key.inventory"),
                        environment.getProperty("application.jwt.secret")),
                instrument.getId());

        instrument.setQuantity(inventory);
        return instrument;
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'SERVICE')")
    public List<InstrumentResponseDto> getInstrumentsByIds(List<String> instrumentIds) {
        return instrumentRepository.findByIdIn(instrumentIds)
                .stream()
                .map(instrumentResponseMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('SELLER')")
    public InstrumentResponseDto update(String  id, InstrumentCreateDto instrumentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors().toString());
        }
        Instrument instrument = instrumentCreateMapper.toEntity(instrumentDTO);
        return instrumentRepository.findById(id)
                .map(repInstrument -> {
                    checkInstrument(instrument, repInstrument);
                    instrumentCreateMapper.update(instrument, repInstrument);
                    return repInstrument;
                })
                .map(this::saveInstrument)
                .map(instrumentResponseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage()));
    }

    private Instrument saveInstrument(Instrument instrument) {
        try {
            Instrument saved = instrumentRepository.save(instrument);
            setAvailability(saved);
            return saved;
        } catch (DataAccessException e) {
            log.error("Failed to update instrument: {}", e.getMessage());
            throw new RuntimeException("Failed to update instrument");
        } catch (WebClientResponseException e) {
            log.error("Failed to set availability: {}", e.getMessage());
            instrumentRepository.delete(instrument);
            throw new RuntimeException("Failed to set availability");
        }
    }

    private void checkInstrument(Instrument instrument, Instrument oldInstrument) {
        List<FieldError> errors = new ArrayList<>();

        checkExistingInstrument(instrumentRepository.findByTitle(instrument.getTitle()), oldInstrument.getId(), "title")
                .ifPresent(errors::add);

        checkExistingInstrument(instrumentRepository.findByDescription(instrument.getDescription()), oldInstrument.getId(), "description")
                .ifPresent(errors::add);

        if (!errors.isEmpty()) {
            throw new InstrumentConflictException(errors);
        }
    }

    private Optional<FieldError> checkExistingInstrument(Optional<Instrument> existingInstrument, String oldInstrumentId, String fieldName) {
        if (existingInstrument.isPresent() && !existingInstrument.get().getId().equals(oldInstrumentId)) {
            return Optional.of(new FieldError("instrument", fieldName,
                    String.format(InstrumentConstants.INSTRUMENT_FIELD_EXISTS.getMessage(), fieldName)));
        }
        return Optional.empty();
    }

    @PreAuthorize("hasRole('SELLER')")
    public void delete(String id) {
        try {
            instrumentRepository.deleteById(id);
            removeAvailability(id);
        } catch (DataAccessException e) {
            log.error("Failed to delete instrument: {}", e.getMessage());
            throw new RuntimeException("Failed to delete instrument");
        }
    }

    private void removeAvailability(String id) {
        inventoryClient.removeInventory("Bearer %s%s".formatted(
                environment.getProperty("api.key.inventory"),
                environment.getProperty("application.jwt.secret")),
                id);
    }
}
