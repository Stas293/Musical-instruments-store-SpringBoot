package org.projects.orderservice.repository;

import org.projects.orderservice.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Page<Order> findAllByUser(String user, Pageable pageable);

    Page<Order> findAll(Pageable pageable);
}
