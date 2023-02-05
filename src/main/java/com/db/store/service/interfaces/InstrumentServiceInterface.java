package com.db.store.service.interfaces;

import com.db.store.model.Instrument;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface InstrumentServiceInterface {
    Instrument getById(Long id);

    Instrument create(Instrument instrument);

    Optional<Instrument> getByTitle(String title);

    Instrument update(Long id, Instrument instrument);

    Optional<Instrument> getByDescription(String description);

    @Transactional
    void delete(Long id);

    Page<Instrument> getPage(int page, int size);
}
