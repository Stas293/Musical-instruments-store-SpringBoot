package org.projects.instrumentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.instrumentservice.dto.InstrumentCreateDto;
import org.projects.instrumentservice.dto.InstrumentResponseDto;
import org.projects.instrumentservice.exception.ValidationException;
import org.projects.instrumentservice.service.InstrumentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createInstrument(@RequestBody @Validated InstrumentCreateDto instrumentDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Validation error: {}", bindingResult.getAllErrors());
            throw new ValidationException(bindingResult.getFieldErrors().toString());
        }

        instrumentService.createInstrument(instrumentDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<InstrumentResponseDto> getAllInstruments(@RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "5") int size) {
        return instrumentService.getInstrumentsPage(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InstrumentResponseDto getInstrumentById(@PathVariable String id) {
        return instrumentService.getInstrumentById(id);
    }
}
