package com.db.store.repository;

import com.db.store.model.InstrumentOrder;
import com.db.store.model.InstrumentOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentOrderRepository extends JpaRepository<InstrumentOrder, InstrumentOrderId> {
    void deleteAllByOrderId(Long id);
}