package org.projects.inventoryservice.repository;

import org.projects.inventoryservice.model.Inventory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Optional<Inventory> findByInstrumentId(String instrumentId);

    List<Inventory> findByInstrumentIdIn(List<String> instrumentIds);
}
