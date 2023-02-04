package com.db.store.repository;

import com.db.store.model.OrderHistory;
import com.db.store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    Optional<OrderHistory> findByUserAndId(User user, Long id);

    Page<OrderHistory> findAllByUser(User user, Pageable of);
}