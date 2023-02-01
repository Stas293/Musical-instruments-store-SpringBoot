package com.db.store.controller;

import com.db.store.dto.InstrumentDTO;
import com.db.store.model.Instrument;
import com.db.store.service.InstrumentService;
import com.db.store.utils.ObjectMapper;
import com.db.store.validation.InstrumentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InstrumentController {
    private final InstrumentService instrumentService;
    private final ObjectMapper objectMapper;
    private final InstrumentValidator instrumentValidator;

    @Autowired
    public InstrumentController(InstrumentService instrumentService,
                                ObjectMapper objectMapper,
                                InstrumentValidator instrumentValidator) {
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.instrumentValidator = instrumentValidator;
    }

    @GetMapping("/user/instruments")
    public ResponseEntity<List<InstrumentDTO>> getInstruments() {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        instrumentService.getAll(), InstrumentDTO.class));
    }

    @GetMapping("/user/instruments/{id}")
    public ResponseEntity<InstrumentDTO> getInstrumentById(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        instrumentService.getById(id), InstrumentDTO.class));
    }

    @PostMapping("/seller/instruments")
    public ResponseEntity<InstrumentDTO> createInstrument(@RequestBody @Valid InstrumentDTO instrumentDTO,
                                                          BindingResult bindingResult) throws MethodArgumentNotValidException {
        Instrument instrument = objectMapper.map(instrumentDTO, Instrument.class);
        instrumentValidator.validate(instrument, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getDeclaredMethods()[0], 0),
                    bindingResult
            );
        }
        return ResponseEntity.ok(
                objectMapper.map(
                        instrumentService.create(instrument), InstrumentDTO.class));
    }

    @PutMapping("/seller/instruments/{id}")
    public ResponseEntity<InstrumentDTO> updateInstrument(@PathVariable Long id,
                                                         @RequestBody @Valid InstrumentDTO instrumentDTO) {
        Instrument instrument = objectMapper.map(instrumentDTO, Instrument.class);
        return ResponseEntity.ok(
                objectMapper.map(
                        instrumentService.update(id, instrument), InstrumentDTO.class));
    }

    @DeleteMapping("/seller/instruments/{id}")
    public ResponseEntity<String> deleteInstrument(@PathVariable Long id) {
        instrumentService.delete(id);
        return ResponseEntity.ok("Instrument with id " + id + " was deleted");
    }
}
