package org.projects.instrumentservice.repository;

import org.projects.instrumentservice.model.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InstrumentRepository extends MongoRepository<Instrument, String> {
    List<Instrument> findByIdIn(List<String> instrumentIds);
}
