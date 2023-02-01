package com.db.store.service;

import com.db.store.constants.InstrumentConstants;
import com.db.store.constants.StatusConstants;
import com.db.store.exceptions.InstrumentConflictException;
import com.db.store.exceptions.InstrumentNotFoundException;
import com.db.store.exceptions.InstrumentReferenceException;
import com.db.store.model.Instrument;
import com.db.store.repository.InstrumentRepository;
import com.db.store.repository.StatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstrumentService {
    private final InstrumentRepository instrumentRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public InstrumentService(InstrumentRepository instrumentRepository,
                             StatusRepository statusRepository) {
        this.instrumentRepository = instrumentRepository;
        this.statusRepository = statusRepository;
    }

    public List<Instrument> getAll() {
        return instrumentRepository.findAll();
    }

    public Instrument getById(Long id) {
        return instrumentRepository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException(
                        InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage()));
    }

    public Instrument create(Instrument instrument) {
        instrument.setDateCreated(LocalDateTime.now());
        instrument.setDateUpdated(LocalDateTime.now());
        instrument.setStatus(statusRepository.findByCode(instrument.getStatus().getCode())
                .orElseThrow(() -> new InstrumentNotFoundException(
                        StatusConstants.STATUS_NOT_FOUND.getMessage())));
        return instrumentRepository.save(instrument);
    }

    public Optional<Instrument> getByTitle(String title) {
        return instrumentRepository.findByTitle(title);
    }

    public Instrument update(Long id, Instrument instrument) {
        Optional<Instrument> repInstrument = instrumentRepository.findById(id);
        if (repInstrument.isPresent()) {
            Instrument oldInstrument = repInstrument.get();
            checkInstrument(instrument, oldInstrument);
            updateInstrument(instrument, oldInstrument);
            return instrumentRepository.save(oldInstrument);
        }
        throw new InstrumentNotFoundException(InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage());
    }

    private void updateInstrument(Instrument instrument, Instrument oldInstrument) {
        oldInstrument.setTitle(instrument.getTitle());
        oldInstrument.setDescription(instrument.getDescription());
        oldInstrument.setPrice(instrument.getPrice());
        oldInstrument.setStatus(
                statusRepository.findByCode(instrument.getStatus().getCode())
                        .orElseThrow(() -> new InstrumentNotFoundException(
                                StatusConstants.STATUS_NOT_FOUND.getMessage())));
        oldInstrument.setDateUpdated(LocalDateTime.now());
    }

    private void checkInstrument(Instrument instrument, Instrument oldInstrument) {
        Optional<Instrument> instrumentByTitle = instrumentRepository.findByTitle(instrument.getTitle());
        Optional<Instrument> instrumentByDescription = instrumentRepository.findByDescription(instrument.getDescription());
        List<FieldError> errors = new ArrayList<>();
        if (instrumentByTitle.isPresent() && !instrumentByTitle.get().getId().equals(oldInstrument.getId())) {
            errors.add(
                    new FieldError(
                            "instrument",
                            "title",
                            InstrumentConstants.INSTRUMENT_TITLE_EXISTS.getMessage()));
        }
        if (instrumentByDescription.isPresent() && !instrumentByDescription.get().getId().equals(oldInstrument.getId())) {
            errors.add(
                    new FieldError(
                            "instrument",
                            "description",
                            InstrumentConstants.INSTRUMENT_DESCRIPTION_EXISTS.getMessage()));
        }
        if (!errors.isEmpty()) {
            throw new InstrumentConflictException(errors);
        }
    }

    public Optional<Instrument> getByDescription(String description) {
        return instrumentRepository.findByDescription(description);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Instrument> instrument = instrumentRepository.findById(id);
        if (instrument.isPresent()) {
            if (!instrument.get().getInstrumentOrders().isEmpty()) {
                throw new InstrumentReferenceException(
                        InstrumentConstants.INSTRUMENT_HAS_ORDERS.getMessage());
            }
            instrumentRepository.delete(instrument.get());
        } else {
            throw new InstrumentNotFoundException(InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage());
        }
    }
}
