package org.projects.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.inventoryservice.model.Inventory;
import org.projects.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public Integer getInventory(String instrumentId) {
        return inventoryRepository.findByInstrumentId(instrumentId)
                .map(Inventory::getQuantity)
                .orElse(0);
    }
}
