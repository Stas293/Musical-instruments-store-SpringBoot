package com.db.store.repository;

import com.db.store.model.OrderHistory;
import com.db.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findAllByUserAndId(User user, Long id);

    List<OrderHistory> findAllByUser(User user);
}