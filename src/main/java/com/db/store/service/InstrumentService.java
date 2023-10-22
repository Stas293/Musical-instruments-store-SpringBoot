package com.db.store.service;

import com.db.store.dto.InstrumentDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

public interface InstrumentService {
    InstrumentDTO getById(Long id);

    InstrumentDTO create(InstrumentDTO instrument, BindingResult bindingResult);

    InstrumentDTO update(Long id, @Valid InstrumentDTO instrument, BindingResult bindingResult);

    @Transactional
    void delete(Long id);

    Page<InstrumentDTO> getPage(int page, int size);
}
