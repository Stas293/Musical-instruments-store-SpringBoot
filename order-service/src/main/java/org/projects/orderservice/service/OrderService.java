package org.projects.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.orderservice.constants.DefaultConstants;
import org.projects.orderservice.dto.OrderCreationDto;
import org.projects.orderservice.dto.OrderHistoryCreationDto;
import org.projects.orderservice.dto.OrderResponseDto;
import org.projects.orderservice.dto.StatusResponseDto;
import org.projects.orderservice.exception.OrderCreationException;
import org.projects.orderservice.exception.ResourceNotFoundException;
import org.projects.orderservice.mapper.OrderHistoryCreationDtoMapper;
import org.projects.orderservice.mapper.OrderCreateDtoMapper;
import org.projects.orderservice.mapper.OrderResponseDtoMapper;
import org.projects.orderservice.mapper.StatusResponseDtoMapper;
import org.projects.orderservice.model.InstrumentOrder;
import org.projects.orderservice.model.NextStatus;
import org.projects.orderservice.model.Order;
import org.projects.orderservice.model.Status;
import org.projects.orderservice.repository.OrderRepository;
import org.projects.orderservice.repository.StatusRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    private final StatusResponseDtoMapper statusResponseDtoMapper;
    private final OrderHistoryCreationDtoMapper orderHistoryCreationDtoMapper;
    private final WebClient.Builder webClientBuilder;

    public Long createOrder(OrderCreationDto orderDto) {
        log.info("Creating order: {}", orderDto);
        return Optional.of(orderCreateDtoMapper.toEntity(orderDto))
                .map(entity -> {
                    entity.setStatusId(statusRepository.findByCode(
                            DefaultConstants.DEFAULT_STATUS.getValue()).get().getId());
                    return entity;
                })
                .map(order -> {
                    order.setUser("Admin");
                    order.getInstrumentOrders().forEach(instrumentOrder -> instrumentOrder.setPrice(BigDecimal.valueOf(293.0)));
                    return order;
                })
                .map(this::checkAvailability)
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

    private Order checkAvailability(Order order) {
        List<String> instrumentIds = order.getInstrumentOrders().stream()
                .map(InstrumentOrder::getInstrumentId)
                .toList();
        log.info("Getting inventory for instrument ids: {}", instrumentIds);
        Map<String, Integer> availabilityMap = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("instrumentIds", instrumentIds)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Integer>>() {
                })
                .block();
        log.info("Received availability map: {}", availabilityMap);
        order.getInstrumentOrders().forEach(instrumentOrder -> {
            Integer quantity = availabilityMap.get(instrumentOrder.getInstrumentId());
            if (quantity == null || quantity < instrumentOrder.getQuantity()) {
                throw new OrderCreationException(
                        "Not enough inventory for instrument: %s"
                                .formatted(instrumentOrder.getInstrumentId()));
            }
        });
        return order;
    }

    public OrderResponseDto updateOrder(Long id, String status) {
        log.info("Updating order with id: {} to status: {}", id, status);
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }
        Optional<Status> currentStatus = statusRepository.findById(order.get().getStatusId());
        if (currentStatus.isEmpty()) {
            throw new ResourceNotFoundException("Status not found");
        }
        Optional<Status> newStatus = statusRepository.findByCode(status);
        if (newStatus.isEmpty()) {
            throw new ResourceNotFoundException("Status not found");
        }
        if (currentStatus.get().getNextStatuses().stream()
                .noneMatch(nextStatus -> nextStatus.getNextStatusId().equals(newStatus.get().getId()))) {
            throw new OrderCreationException("Invalid status transition");
        }
        order.get().setStatusId(newStatus.get().getId());
        order.get().setStatus(newStatus.get());
        if (newStatus.get().getClosed()) {
            order.get().setClosed(true);
            OrderHistoryCreationDto orderHistoryCreationDto = orderHistoryCreationDtoMapper.toDto(order.get());
            String historyOrderId = webClientBuilder.build().post()
                    .uri("http://history-order-service/api/order-history")
                    .bodyValue(orderHistoryCreationDto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Created history order with id: {}", historyOrderId);

            orderRepository.deleteById(id);
            return orderResponseDtoMapper.toDto(order.get());
        } else {
            return orderResponseDtoMapper.toDto(orderRepository.save(order.get()));
        }
    }

    public List<StatusResponseDto> getNextStatuses(Long id) {
        log.info("Getting next statuses for order with id: {}", id);
        return orderRepository.findById(id)
                .map(order -> statusRepository.findById(order.getStatusId())
                        .orElseThrow(() -> new ResourceNotFoundException("Status not found")))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"))
                .getNextStatuses().stream()
                .map(NextStatus::getNextStatusId)
                .map(statusRepository::findById)
                .map(Optional::get)
                .map(statusResponseDtoMapper::toDto)
                .toList();

    }
}
