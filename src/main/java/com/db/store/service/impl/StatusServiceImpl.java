package com.db.store.service.impl;

import com.db.store.constants.StatusConstants;
import com.db.store.dto.StatusDTO;
import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.mapper.StatusDtoMapper;
import com.db.store.model.Status;
import com.db.store.repository.StatusRepository;
import com.db.store.service.StatusService;
import com.db.store.utils.ExceptionParser;
import com.db.store.validation.StatusValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    private final StatusDtoMapper statusMapper;
    private final StatusValidator statusValidator;

    private static void updateStatus(Status status, Status oldStatus) {
        oldStatus.setCode(status.getCode());
        oldStatus.setName(status.getName());
        oldStatus.setClosed(status.getClosed());
    }

    @Override
    public Page<StatusDTO> getStatuses(int page, int size) {
        return statusRepository.findAll(PageRequest.of(page, size))
                .map(statusMapper::toDto);
    }

    @Override
    @Transactional
    public StatusDTO saveStatus(StatusDTO statusDTO, BindingResult bindingResult) {
        statusValidator.validate(statusDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
        Status status = statusMapper.toEntity(statusDTO);
        return statusMapper.toDto(statusRepository.save(status));
    }

    @Override
    public StatusDTO getStatusById(Long id) {
        return statusRepository.findById(id)
                .map(statusMapper::toDto)
                .orElseThrow(ExceptionParser.exceptionSupplier(
                        StatusNotFoundException.class,
                        StatusConstants.STATUS_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public StatusDTO updateStatusById(Long id, StatusDTO statusDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
        Status status = statusMapper.toEntity(statusDTO);
        Optional<Status> repStatus = statusRepository.findById(id);
        if (repStatus.isPresent()) {
            Status oldStatus = repStatus.get();
            checkStatus(status, oldStatus, bindingResult);
            updateStatus(status, oldStatus);
            return statusMapper.toDto(statusRepository.save(oldStatus));
        }
        throw new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage());
    }

    private void checkStatus(Status status, Status oldStatus, BindingResult bindingResult) {
        Optional<Status> statusByName = getStatusByName(status.getName());
        Optional<Status> statusByCode = getStatusByCode(status.getCode());

        Optional<FieldError> nameError = checkExistingStatus(statusByName, oldStatus.getId(), "name");
        Optional<FieldError> codeError = checkExistingStatus(statusByCode, oldStatus.getId(), "code");

        nameError.ifPresent(bindingResult::addError);
        codeError.ifPresent(bindingResult::addError);

        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
    }

    private Optional<FieldError> checkExistingStatus(Optional<Status> statusByName, Long id, String name) {
        if (statusByName.isPresent() && !statusByName.get().getId().equals(id)) {
            return Optional.of(
                    new FieldError(
                            "status",
                            name,
                            String.format(StatusConstants.STATUS_FIELD_EXISTS.getMessage(), name)));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteStatusById(Long id) throws StatusNotFoundException {
        statusRepository.findById(id)
                .ifPresentOrElse(
                        statusRepository::delete,
                        () -> {
                            throw new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage());
                        });
    }

    @Override
    public Optional<Status> getStatusByName(String name) {
        return statusRepository.findByName(name);
    }

    @Override
    public Optional<Status> getStatusByCode(String code) {
        return statusRepository.findByCode(code);
    }
}
