package com.db.store.controller;

import com.db.store.dto.InstrumentDTO;
import com.db.store.service.InstrumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InstrumentController {
    private final InstrumentService instrumentService;

    @GetMapping("/user/instruments")
    @ResponseStatus(HttpStatus.OK)
    public Page<InstrumentDTO> getInstruments(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "5") int size) {
        return instrumentService.getPage(page, size);
    }

    @GetMapping("/user/instruments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InstrumentDTO getInstrumentById(@PathVariable Long id) {
        return instrumentService.getById(id);
    }

    @PostMapping("/seller/instruments")
    @ResponseStatus(HttpStatus.CREATED)
    public InstrumentDTO createInstrument(@RequestBody @Validated InstrumentDTO instrumentDTO,
                                          BindingResult bindingResult) {
        return instrumentService.create(instrumentDTO, bindingResult);
    }

    @PutMapping("/seller/instruments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InstrumentDTO updateInstrument(@PathVariable Long id,
                                          @RequestBody @Validated InstrumentDTO instrumentDTO,
                                          BindingResult bindingResult) {
        return instrumentService.update(id, instrumentDTO, bindingResult);
    }

    @DeleteMapping("/seller/instruments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstrument(@PathVariable Long id) {
        instrumentService.delete(id);
    }
}
