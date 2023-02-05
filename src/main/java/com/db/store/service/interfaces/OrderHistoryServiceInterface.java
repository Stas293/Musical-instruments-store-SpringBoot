package com.db.store.service.interfaces;

import com.db.store.model.OrderHistory;
import org.springframework.data.domain.Page;

public interface OrderHistoryServiceInterface {
    Page<OrderHistory> getAllOrderHistories(int page, int size);

    OrderHistory getOrderHistoryByIdForUser(Long id);

    Page<OrderHistory> getAllOrderHistoriesByUser(int page, int size);

    OrderHistory getOrderHistoryById(Long id);
}
