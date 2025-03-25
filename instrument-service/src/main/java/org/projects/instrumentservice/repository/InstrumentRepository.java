package org.projects.instrumentservice.repository;

import org.projects.instrumentservice.model.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends MongoRepository<Instrument, String> {
    List<Instrument> findByIdIn(List<String> instrumentIds);

    Optional<Instrument> findByTitle(String title);

    Optional<Instrument> findByDescription(String description);
}
