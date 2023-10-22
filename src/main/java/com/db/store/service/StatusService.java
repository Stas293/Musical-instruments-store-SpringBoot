package com.db.store.service;

import com.db.store.dto.StatusDTO;
import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface StatusService {
    Page<StatusDTO> getStatuses(int page, int size);

    StatusDTO saveStatus(StatusDTO status, BindingResult bindingResult);

    StatusDTO getStatusById(Long id) throws StatusNotFoundException;

    StatusDTO updateStatusById(Long id, StatusDTO status, BindingResult bindingResult);

    void deleteStatusById(Long id) throws StatusNotFoundException;

    Optional<Status> getStatusByName(String name);

    Optional<Status> getStatusByCode(String code);
}
