package com.db.store.controller;

import com.db.store.dto.StatusDTO;
import com.db.store.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/status")
@AllArgsConstructor
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<StatusDTO> getStatuses(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "5") int size) {
        return statusService.getStatuses(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO createStatus(@RequestBody @Validated StatusDTO statusDTO,
                                  BindingResult bindingResult) {
        return statusService.saveStatus(statusDTO, bindingResult);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO getStatus(@PathVariable Long id) {
        return statusService.getStatusById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO updateStatus(@PathVariable Long id,
                                  @Validated @RequestBody StatusDTO statusDTO,
                                  BindingResult bindingResult) {
        return statusService.updateStatusById(id, statusDTO, bindingResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStatus(@PathVariable Long id) {
        statusService.deleteStatusById(id);
    }
}
