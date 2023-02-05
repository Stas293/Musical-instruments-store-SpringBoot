package com.db.store.service.interfaces;

import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.model.Status;
import org.springframework.data.domain.Page;

public interface StatusServiceInterface {
    Page<Status> getStatuses(int page, int size);

    Status saveStatus(Status status);

    Status getStatusById(Long id) throws StatusNotFoundException;

    Status updateStatusById(Long id, Status status);

    Status deleteStatusById(Long id) throws StatusNotFoundException;

    Status getStatusByName(String name);

    Status getStatusByCode(String code);
}
