package org.projects.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.inventoryservice.model.Inventory;
import org.projects.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Integer> getInventoryByInstrumentIds(List<String> instrumentIds) {
        return inventoryRepository.findByInstrumentIdIn(instrumentIds)
                .stream()
                .collect(Collectors.toMap(Inventory::getInstrumentId, Inventory::getQuantity));
    }
}
