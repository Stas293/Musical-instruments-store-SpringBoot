package com.db.store.controller;

import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.model.Status;
import com.db.store.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/status")
    public ResponseEntity<List<Status>> getStatuses() {
        return ResponseEntity.ok().body(statusService.getStatuses());
    }

    @PostMapping("/status")
    public ResponseEntity<Status> createStatus(@Valid @RequestBody Status status) {
        return ResponseEntity.ok().body(statusService.saveStatus(status));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Optional<Status>> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok().body(statusService.getStatusById(id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Long id, @Valid @RequestBody Status status) {
        return ResponseEntity.ok().body(statusService.updateStatusById(id, status));
    }

    @DeleteMapping("/status/{id}")
    public ResponseEntity<Status> deleteStatus(@PathVariable Long id) {
        return ResponseEntity.ok().body(statusService.deleteStatusById(id));
    }
}
