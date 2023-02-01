package com.db.store.service;

import com.db.store.constants.UserConstants;
import com.db.store.exceptions.UserNotFoundException;
import com.db.store.model.OrderHistory;
import com.db.store.model.User;
import com.db.store.repository.OrderHistoryRepository;
import com.db.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository,
                               UserRepository userRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<OrderHistory> getAllOrderHistories() {
        return orderHistoryRepository.findAll();
    }

    public List<OrderHistory> getAllOrderHistoriesByIdForUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findAllByUserAndId(user, id);
    }

    public List<OrderHistory> getAllOrderHistoriesByUser() {
        User user = userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findAllByUser(user);
    }

    public OrderHistory getOrderHistoryById(Long id) {
        return orderHistoryRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
    }
}
