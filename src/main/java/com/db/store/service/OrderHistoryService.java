package com.db.store.service;

import com.db.store.constants.UserConstants;
import com.db.store.service.interfaces.OrderHistoryServiceInterface;
import com.db.store.exceptions.UserNotFoundException;
import com.db.store.model.OrderHistory;
import com.db.store.model.User;
import com.db.store.repository.OrderHistoryRepository;
import com.db.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderHistoryService implements OrderHistoryServiceInterface {
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository,
                               UserRepository userRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<OrderHistory> getAllOrderHistories(int page, int size) {
        return orderHistoryRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public OrderHistory getOrderHistoryByIdForUser(Long id) {
        User user = userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findByUserAndId(user, id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
    }

    @Override
    public Page<OrderHistory> getAllOrderHistoriesByUser(int page, int size) {
        User user = userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findAllByUser(user, PageRequest.of(page, size));
    }

    @Override
    public OrderHistory getOrderHistoryById(Long id) {
        return orderHistoryRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
    }
}
