package com.db.store.service;

import com.db.store.dto.OrderHistoryDTO;
import org.springframework.data.domain.Page;

public interface OrderHistoryService {
    Page<OrderHistoryDTO> getAllOrderHistories(int page, int size);

    OrderHistoryDTO getOrderHistoryByIdForUser(Long id);

    Page<OrderHistoryDTO> getAllOrderHistoriesByUser(int page, int size);

    OrderHistoryDTO getOrderHistoryById(Long id);
}
