package com.db.store.service.impl;

import com.db.store.constants.InstrumentConstants;
import com.db.store.constants.StatusConstants;
import com.db.store.dto.InstrumentDTO;
import com.db.store.exceptions.InstrumentConflictException;
import com.db.store.exceptions.InstrumentNotFoundException;
import com.db.store.exceptions.InstrumentReferenceException;
import com.db.store.mapper.InstrumentDtoMapper;
import com.db.store.model.Instrument;
import com.db.store.model.Status;
import com.db.store.repository.InstrumentRepository;
import com.db.store.repository.StatusRepository;
import com.db.store.service.InstrumentService;
import com.db.store.utils.ExceptionParser;
import com.db.store.validation.InstrumentValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.db.store.constants.InstrumentConstants.INSTRUMENT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {
    private final InstrumentRepository instrumentRepository;
    private final StatusRepository statusRepository;
    private final InstrumentDtoMapper instrumentDtoMapper;
    private final InstrumentValidator instrumentValidator;

    @Override
    public InstrumentDTO getById(Long id) {
        return instrumentRepository.findById(id)
                .map(instrumentDtoMapper::toDto)
                .orElseThrow(() -> new InstrumentNotFoundException(
                        INSTRUMENT_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public InstrumentDTO create(InstrumentDTO instrumentDTO, BindingResult bindingResult) {
        instrumentValidator.validate(instrumentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
        Status status = statusRepository.findByCode(instrumentDTO.getStatus().getCode())
                .orElseThrow(() -> new InstrumentNotFoundException(
                        StatusConstants.STATUS_NOT_FOUND.getMessage()));
        return Optional.of(instrumentDtoMapper.toEntity(instrumentDTO))
                .map(instrument -> {
                            instrument.setStatus(status);
                            return instrument;
                        }
                )
                .map(instrumentRepository::save)
                .map(instrumentDtoMapper::toDto)
                .orElseThrow(() -> new InstrumentNotFoundException(
                        INSTRUMENT_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public InstrumentDTO update(Long id, InstrumentDTO instrumentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
        Instrument instrument = instrumentDtoMapper.toEntity(instrumentDTO);
        return instrumentRepository.findById(id)
                .map(repInstrument -> {
                    checkInstrument(instrument, repInstrument);
                    instrumentDtoMapper.update(instrument, repInstrument);
                    return repInstrument;
                })
                .map(instrumentRepository::save)
                .map(instrumentDtoMapper::toDto)
                .orElseThrow(() -> new InstrumentNotFoundException(
                        INSTRUMENT_NOT_FOUND.getMessage()));
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

    private Optional<FieldError> checkExistingInstrument(Optional<Instrument> existingInstrument, Long oldInstrumentId, String fieldName) {
        if (existingInstrument.isPresent() && !existingInstrument.get().getId().equals(oldInstrumentId)) {
            return Optional.of(new FieldError("instrument", fieldName,
                    String.format(InstrumentConstants.INSTRUMENT_FIELD_EXISTS.getMessage(), fieldName)));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        instrumentRepository.findById(id)
                .map(instrument -> {
                    if (!instrument.getInstrumentOrders().isEmpty()) {
                        ExceptionParser.exceptionSupplier(InstrumentReferenceException.class,
                                InstrumentConstants.INSTRUMENT_HAS_ORDERS.getMessage());
                    }
                    return instrument;
                })
                .ifPresentOrElse(instrumentRepository::delete, () -> ExceptionParser.exceptionSupplier(
                        InstrumentNotFoundException.class,
                        INSTRUMENT_NOT_FOUND.getMessage())
                );
    }

    @Override
    public Page<InstrumentDTO> getPage(int page, int size) {
        return instrumentRepository.findAll(PageRequest.of(page, size))
                .map(instrumentDtoMapper::toDto);
    }
}
