package org.projects.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.orderservice.constants.DefaultConstants;
import org.projects.orderservice.dto.OrderCreationDto;
import org.projects.orderservice.dto.OrderResponseDto;
import org.projects.orderservice.exception.OrderCreationException;
import org.projects.orderservice.exception.ResourceNotFoundException;
import org.projects.orderservice.mapper.OrderCreateDtoMapper;
import org.projects.orderservice.mapper.OrderResponseDtoMapper;
import org.projects.orderservice.model.Order;
import org.projects.orderservice.repository.OrderRepository;
import org.projects.orderservice.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final OrderCreateDtoMapper orderCreateDtoMapper;
    private final OrderResponseDtoMapper orderResponseDtoMapper;

    public Long createOrder(OrderCreationDto orderDto) {
        log.info("Creating order: {}", orderDto);
        return Optional.of(orderCreateDtoMapper.toEntity(orderDto))
                .map(entity -> {
                    entity.setStatusId(statusRepository.findByCode(
                            DefaultConstants.DEFAULT_STATUS.getValue()).getId());
                    return entity;
                })
                .map(order -> {
                    order.setUser("Admin");
                    order.getInstrumentOrders().forEach(instrumentOrder -> instrumentOrder.setPrice(BigDecimal.valueOf(293.0)));
                    return order;
                })
                .map(orderRepository::save)
                .map(Order::getId)
                .orElseThrow(() -> new OrderCreationException("Failed to create order"));
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long id) {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(statusRepository.findById(order.getStatusId())
                            .orElseThrow(() -> new ResourceNotFoundException("Status not found")));
                    return order;
                })
                .map(orderResponseDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
}
