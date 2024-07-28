package org.projects.instrumentservice.repository;

import org.projects.instrumentservice.model.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstrumentRepository extends MongoRepository<Instrument, String> {
}
