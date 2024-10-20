package org.projects.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{instrumentId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getInventory(@PathVariable String instrumentId) {
        log.info("Finding inventory for instrument id: {}", instrumentId);
        return inventoryService.getInventory(instrumentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> getInventoryByInstrumentIds(@RequestParam List<String> instrumentIds) {
        log.info("Finding inventory for instrument ids: {}", instrumentIds);
        return inventoryService.getInventoryByInstrumentIds(instrumentIds);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void changeInventory(@RequestBody Map<String, Integer> instrumentIdToQuantity) {
        log.info("Changing inventory: {}", instrumentIdToQuantity);
        inventoryService.changeInventory(instrumentIdToQuantity);
    }

    @PostMapping("/{instrumentId}")
    @ResponseStatus(HttpStatus.OK)
    public void setInventory(@PathVariable String instrumentId, @RequestParam Integer quantity) {
        log.info("Setting inventory for instrument id: {} to quantity: {}", instrumentId, quantity);
        inventoryService.setInventory(instrumentId, quantity);
    }

    @DeleteMapping ("/api/inventory/{instrumentId}")
    public void removeInventory(@PathVariable String instrumentId) {
        log.info("Removing inventory for instrument id: {}", instrumentId);
        inventoryService.removeInventory(instrumentId);
    }
}
