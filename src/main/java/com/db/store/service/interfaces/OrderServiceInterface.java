package com.db.store.service.interfaces;

import com.db.store.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

public interface OrderServiceInterface {
    @Transactional
    Order create(Order order);

    Page<Order> getAllOrdersForLoggedUser(int page, int size);

    Order getOrderForUserById(Long id);

    Page<Order> getAllOrders(int page, int size);

    @Transactional
    Order setNextStatus(Long id);

    @Transactional
    Order cancelOrder(Long id);

    @Transactional
    Order updateOrder(Long id, Order order);
}
