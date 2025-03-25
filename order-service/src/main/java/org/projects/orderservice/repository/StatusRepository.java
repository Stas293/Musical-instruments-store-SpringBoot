package org.projects.orderservice.repository;

import org.projects.orderservice.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StatusRepository extends CrudRepository<Status, Long> {
    Optional<Status> findByCode(String code);
}
