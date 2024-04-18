package com.db.store.repository;

import com.db.store.model.Order;
import com.db.store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable of);

    Optional<Order> findByIdAndUser(Long id, User user);
}
