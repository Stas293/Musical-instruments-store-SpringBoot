package com.db.store.service;

import com.db.store.constants.StatusConstants;
import com.db.store.exceptions.StatusConflictException;
import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.model.Status;
import com.db.store.repository.StatusRepository;
import com.db.store.service.interfaces.StatusServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatusService implements StatusServiceInterface {
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Page<Status> getStatuses(int page, int size) {
        return statusRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Status saveStatus(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public Status getStatusById(Long id) throws StatusNotFoundException {
        return statusRepository.findById(id)
                .orElseThrow(
                        () -> new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage()));
    }

    @Override
    public Status updateStatusById(Long id, Status status) {
        Optional<Status> repStatus = statusRepository.findById(id);
        if (repStatus.isPresent()) {
            Status oldStatus = repStatus.get();
            checkStatus(status, oldStatus);
            updateStatus(status, oldStatus);
            return statusRepository.save(oldStatus);
        }
        throw new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage());
    }

    private void checkStatus(Status status, Status oldStatus) {
        Status statusByName = getStatusByName(status.getName());
        Status statusByCode = getStatusByCode(status.getCode());
        List<FieldError> errors = new ArrayList<>();
        if (statusByName != null && !statusByName.getId().equals(oldStatus.getId())) {
            errors.add(
                    new FieldError(
                            "status",
                            "name",
                            StatusConstants.STATUS_NAME_EXISTS.getMessage()));
        }
        if (statusByCode != null && !statusByCode.getId().equals(oldStatus.getId())) {
            errors.add(
                    new FieldError(
                            "status",
                            "code",
                            StatusConstants.STATUS_CODE_EXISTS.getMessage()));
        }
        if (!errors.isEmpty()) {
            throw new StatusConflictException(errors);
        }
    }

    private static void updateStatus(Status status, Status oldStatus) {
        oldStatus.setCode(status.getCode());
        oldStatus.setName(status.getName());
        oldStatus.setClosed(status.isClosed());
    }

    @Override
    public Status deleteStatusById(Long id) throws StatusNotFoundException {
        Optional<Status> repStatus = statusRepository.findById(id);
        if (repStatus.isPresent()) {
            statusRepository.deleteById(id);
            return repStatus.get();
        }
        throw new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage());
    }

    @Override
    public Status getStatusByName(String name) {
        return statusRepository.findByName(name).orElse(null);
    }

    @Override
    public Status getStatusByCode(String code) {
        return statusRepository.findByCode(code).orElse(null);
    }
}
