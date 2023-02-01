package com.db.store.repository;

import com.db.store.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    Optional<Instrument> findByTitle(String title);

    Optional<Instrument> findByDescription(String description);
}
