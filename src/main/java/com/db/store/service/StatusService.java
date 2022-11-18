package com.db.store.service;

import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.model.Status;
import com.db.store.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Status> getStatusById(Long id) throws StatusNotFoundException {
        if (statusRepository.findById(id).isPresent()) {
            return statusRepository.findById(id);
        }
        throw new StatusNotFoundException(ServiceConstants.STATUS_NOT_FOUND);
    }

    public Status updateStatusById(Long id, Status status) throws StatusNotFoundException {
        Optional<Status> repStatus = statusRepository.findById(id);
        if (repStatus.isPresent()) {
            Status oldStatus = repStatus.get();
            updateStatus(status, oldStatus);
            return statusRepository.save(oldStatus);
        }
        throw new StatusNotFoundException(ServiceConstants.STATUS_NOT_FOUND);
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
        throw new StatusNotFoundException(ServiceConstants.STATUS_NOT_FOUND);
    }
}
