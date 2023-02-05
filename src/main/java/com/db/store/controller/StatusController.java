package com.db.store.controller;

import com.db.store.dto.StatusDTO;
import com.db.store.model.Status;
import com.db.store.service.StatusService;
import com.db.store.utils.ObjectMapper;
import com.db.store.validation.StatusValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/status")
public class StatusController {
    private final StatusService statusService;
    private final StatusValidator statusValidator;
    private final ObjectMapper objectMapper;

    @Autowired
    public StatusController(StatusService statusService,
                            StatusValidator statusValidator,
                            ObjectMapper objectMapper) {
        this.statusService = statusService;
        this.statusValidator = statusValidator;
        this.objectMapper = objectMapper;
    }

    @GetMapping()
    public ResponseEntity<List<StatusDTO>> getStatuses(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "5") int size) {
        List<StatusDTO> statusDTOS = objectMapper.mapList(statusService.getStatuses(page, size).getContent(), StatusDTO.class);
        return ResponseEntity.ok().body(statusDTOS);
    }

    @PostMapping()
    public ResponseEntity<StatusDTO> createStatus(@RequestBody @Valid StatusDTO statusDTO,
                                               BindingResult bindingResult) throws MethodArgumentNotValidException {
        Status status = objectMapper.map(statusDTO, Status.class);
        statusValidator.validate(status, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getDeclaredMethods()[0], 0),
                    bindingResult
            );
        }
        return ResponseEntity.ok(
                objectMapper.map(
                        statusService.saveStatus(status), StatusDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        statusService.getStatusById(id), StatusDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusDTO> updateStatus(@PathVariable Long id,
                                               @Valid @RequestBody StatusDTO statusDTO) {
        Status status = objectMapper.map(statusDTO, Status.class);
        return ResponseEntity.ok(
                objectMapper.map(
                        statusService.updateStatusById(id, status), StatusDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDTO> deleteStatus(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        statusService.deleteStatusById(id), StatusDTO.class));
    }
}
