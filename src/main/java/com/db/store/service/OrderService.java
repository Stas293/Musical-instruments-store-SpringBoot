package com.db.store.service;

import com.db.store.dto.OrderDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

public interface OrderService {
    @Transactional
    OrderDTO create(OrderDTO order, BindingResult bindingResult);

    Page<OrderDTO> getAllOrdersForLoggedUser(int page, int size);

    OrderDTO getOrderForUserById(Long id);

    Page<OrderDTO> getAllOrders(int page, int size);

    @Transactional
    OrderDTO setNextStatus(Long id);

    @Transactional
    OrderDTO cancelOrder(Long id);

    @Transactional
    OrderDTO updateOrder(Long id, OrderDTO order);
}
