package org.projects.orderservice.repository;

import org.projects.orderservice.model.Status;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends CrudRepository<Status, Long> {
    Status findByCode(String code);
}
