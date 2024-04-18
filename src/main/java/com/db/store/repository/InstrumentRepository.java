package com.db.store.repository;

import com.db.store.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    Optional<Instrument> findByTitle(String title);

    Optional<Instrument> findByDescription(String description);

    List<Instrument> findByTitleIn(List<String> instrumentTitles);
}
