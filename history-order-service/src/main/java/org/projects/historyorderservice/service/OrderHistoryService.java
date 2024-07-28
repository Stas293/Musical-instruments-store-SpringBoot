package org.projects.historyorderservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.historyorderservice.dto.OrderHistoryCreationDto;
import org.projects.historyorderservice.dto.OrderHistoryResponseDto;
import org.projects.historyorderservice.exception.OrderHistoryCreationException;
import org.projects.historyorderservice.exception.ResourceNotFoundException;
import org.projects.historyorderservice.mapper.OrderHistoryCreationDtoMapper;
import org.projects.historyorderservice.mapper.OrderHistoryResponseDtoMapper;
import org.projects.historyorderservice.model.OrderHistory;
import org.projects.historyorderservice.repository.OrderHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryCreationDtoMapper orderHistoryCreationMapper;
    private final OrderHistoryResponseDtoMapper orderHistoryResponseMapper;

    public Page<OrderHistoryResponseDto> getAllOrderHistories(int page, int size) {
        return orderHistoryRepository.findAll(PageRequest.of(page, size))
                .map(orderHistoryResponseMapper::toDto);
    }

    public OrderHistoryResponseDto getOrderHistoryByIdForUser(String id) {
//        String login = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByLogin(login)
//                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        return orderHistoryRepository.findById(id)
                .map(orderHistoryResponseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order history with id " + id + " not found"));
    }

    public String createOrderHistoryForUser(OrderHistoryCreationDto orderHistoryResponseDto) {
        return Optional.of(orderHistoryCreationMapper.toEntity(orderHistoryResponseDto))
                .map(orderHistoryRepository::save)
                .map(OrderHistory::getId)
                .orElseThrow(() -> new OrderHistoryCreationException("Failed to create order history"));
    }
}
