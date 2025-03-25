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

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInstrument(@RequestBody @Validated InstrumentCreateDto instrumentDto,
                                 BindingResult bindingResult) {
        instrumentService.createInstrument(instrumentDto, bindingResult);
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

    @GetMapping("/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<InstrumentResponseDto> getInventoryByInstrumentIds(@RequestParam List<String> instrumentIds) {
        log.info("Finding instruments for instrument ids: {}", instrumentIds);
        return instrumentService.getInstrumentsByIds(instrumentIds);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InstrumentResponseDto updateInstrument(@PathVariable String id,
                                          @RequestBody @Validated InstrumentCreateDto instrumentDTO,
                                          BindingResult bindingResult) {
        return instrumentService.update(id, instrumentDTO, bindingResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstrument(@PathVariable String id) {
        instrumentService.delete(id);
    }
}
