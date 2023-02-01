package com.db.store.service;

import com.db.store.constants.StatusConstants;
import com.db.store.exceptions.StatusConflictException;
import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.model.Status;
import com.db.store.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> getStatuses() {
        return statusRepository.findAll();
    }

    public Status saveStatus(Status status) {
        return statusRepository.save(status);
    }

    public Status getStatusById(Long id) throws StatusNotFoundException {
        return statusRepository.findById(id)
                .orElseThrow(
                        () -> new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage()));
    }

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

    public Status deleteStatusById(Long id) throws StatusNotFoundException {
        Optional<Status> repStatus = statusRepository.findById(id);
        if (repStatus.isPresent()) {
            statusRepository.deleteById(id);
            return repStatus.get();
        }
        throw new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage());
    }

    public Status getStatusByName(String name) {
        return statusRepository.findByName(name).orElse(null);
    }

    public Status getStatusByCode(String code) {
        return statusRepository.findByCode(code).orElse(null);
    }
}
