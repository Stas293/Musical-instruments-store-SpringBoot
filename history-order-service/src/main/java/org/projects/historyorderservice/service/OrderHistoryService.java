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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryCreationDtoMapper orderHistoryCreationMapper;
    private final OrderHistoryResponseDtoMapper orderHistoryResponseMapper;

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    public Page<OrderHistoryResponseDto> getAllOrderHistories(int page, int size, Principal principal) {
        return orderHistoryRepository.findAllByUser(PageRequest.of(page, size), principal.getName())
                .map(orderHistoryResponseMapper::toDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    public OrderHistoryResponseDto getOrderHistoryByIdForUser(String id, Principal principal) {
        return orderHistoryRepository.findById(id)
                .filter(orderHistory -> {
                    if (principal instanceof Authentication authentication) {
                        return authentication.getAuthorities().stream()
                                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
                                || orderHistory.getUser().equals(principal.getName());
                    }
                    return true;
                })
                .map(orderHistoryResponseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order history with id " + id + " not found"));
    }

    @PreAuthorize("hasAnyRole('SELLER', 'SERVICE')")
    public String createOrderHistoryForUser(OrderHistoryCreationDto orderHistoryResponseDto) {
        return Optional.of(orderHistoryCreationMapper.toEntity(orderHistoryResponseDto))
                .map(orderHistoryRepository::save)
                .map(OrderHistory::getId)
                .orElseThrow(() -> new OrderHistoryCreationException("Failed to create order history"));
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public Page<OrderHistoryResponseDto> getAllOrderHistoriesForUser(int page, int size) {
        log.info("Getting all order histories for users");
        return orderHistoryRepository.findAll(PageRequest.of(page, size))
                .map(orderHistoryResponseMapper::toDto);
    }
}
