package com.db.store.repository;

import com.db.store.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByCode(String code);

    Optional<Status> findByName(String name);
}
