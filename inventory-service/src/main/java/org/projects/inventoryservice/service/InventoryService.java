package org.projects.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.inventoryservice.model.Inventory;
import org.projects.inventoryservice.repository.InventoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'SERVICE')")
    public Integer getInventory(String instrumentId) {
        return inventoryRepository.findByInstrumentId(instrumentId)
                .map(Inventory::getQuantity)
                .orElse(0);
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'SERVICE')")
    public Map<String, Integer> getInventoryByInstrumentIds(List<String> instrumentIds) {
        return inventoryRepository.findByInstrumentIdIn(instrumentIds)
                .stream()
                .collect(Collectors.toMap(Inventory::getInstrumentId, Inventory::getQuantity));
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SERVICE')")
    public void changeInventory(Map<String, Integer> instrumentIdToQuantity) {
        List<Inventory> byInstrumentIdIn = inventoryRepository.findByInstrumentIdIn(instrumentIdToQuantity.keySet());
        byInstrumentIdIn.forEach(inventory -> inventory.setQuantity(
                inventory.getQuantity() - instrumentIdToQuantity.get(inventory.getInstrumentId())));
        inventoryRepository.saveAll(byInstrumentIdIn);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SERVICE')")
    public void setInventory(String instrumentId, Integer quantity) {
        Optional<Inventory> inventory = inventoryRepository.findByInstrumentId(instrumentId);
        if (inventory.isPresent()) {
            inventory.get().setQuantity(quantity);
            inventoryRepository.save(inventory.get());
        } else {
            inventoryRepository.save(Inventory.builder()
                    .instrumentId(instrumentId)
                    .quantity(quantity)
                    .build());
        }
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SERVICE')")
    public void removeInventory(String instrumentId) {
        inventoryRepository.deleteByInstrumentId(instrumentId);
    }
}
