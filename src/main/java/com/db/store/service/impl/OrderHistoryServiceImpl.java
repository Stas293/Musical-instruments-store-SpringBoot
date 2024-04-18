package com.db.store.service.impl;

import com.db.store.dto.OrderHistoryDTO;
import com.db.store.exceptions.UserNotFoundException;
import com.db.store.mapper.OrderHistoryDtoMapper;
import com.db.store.model.User;
import com.db.store.repository.OrderHistoryRepository;
import com.db.store.repository.UserRepository;
import com.db.store.service.OrderHistoryService;
import com.db.store.utils.ExceptionParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.db.store.constants.UserConstants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;
    private final OrderHistoryDtoMapper orderHistoryDtoMapper;

    @Override
    public Page<OrderHistoryDTO> getAllOrderHistories(int page, int size) {
        return orderHistoryRepository.findAll(PageRequest.of(page, size))
                .map(orderHistoryDtoMapper::toDto);
    }

    @Override
    public OrderHistoryDTO getOrderHistoryByIdForUser(Long id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findByUserAndId(user, id)
                .map(orderHistoryDtoMapper::toDto)
                .orElseThrow(ExceptionParser.exceptionSupplier(UserNotFoundException.class, USER_NOT_FOUND.getMessage()));
    }

    @Override
    public Page<OrderHistoryDTO> getAllOrderHistoriesByUser(int page, int size) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findAllByUser(user, PageRequest.of(page, size))
                .map(orderHistoryDtoMapper::toDto);
    }

    @Override
    public OrderHistoryDTO getOrderHistoryById(Long id) {
        return orderHistoryRepository.findById(id)
                .map(orderHistoryDtoMapper::toDto)
                .orElseThrow(ExceptionParser.exceptionSupplier(UserNotFoundException.class, USER_NOT_FOUND.getMessage()));
    }
}
