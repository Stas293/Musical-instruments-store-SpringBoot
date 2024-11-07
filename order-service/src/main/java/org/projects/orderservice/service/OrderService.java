package org.projects.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.orderservice.client.HistoryOrderClient;
import org.projects.orderservice.client.InstrumentClient;
import org.projects.orderservice.client.InventoryClient;
import org.projects.orderservice.constants.DefaultConstants;
import org.projects.orderservice.dto.*;
import org.projects.orderservice.event.OrderCreatedEvent;
import org.projects.orderservice.exception.InventoryUpdateException;
import org.projects.orderservice.exception.OrderCreationException;
import org.projects.orderservice.exception.ResourceNotFoundException;
import org.projects.orderservice.exception.ServiceNotAvailableException;
import org.projects.orderservice.mapper.*;
import org.projects.orderservice.model.InstrumentOrder;
import org.projects.orderservice.model.NextStatus;
import org.projects.orderservice.model.Order;
import org.projects.orderservice.model.Status;
import org.projects.orderservice.repository.OrderRepository;
import org.projects.orderservice.repository.StatusRepository;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final InstrumentClient instrumentClient;
    private final Environment environment;
    private final InventoryClient inventoryClient;
    private final HistoryOrderClient historyOrderClient;
    private final InstrumentOrderResponseDtoMapper instrumentOrderResponseDtoMapper;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    public Long createOrder(OrderCreationDto orderDto, Principal principal, String email) {
        log.info("Creating order: {}", orderDto);
        return Optional.of(orderCreateDtoMapper.toEntity(orderDto))
                .map(entity -> {
                    entity.setStatusId(statusRepository.findByCode(
                            DefaultConstants.DEFAULT_STATUS.getValue()).get().getId());
                    return entity;
                })
                .map(this::checkAvailability)
                .map(order -> setOrderFieldsFromInstrumentRepository(principal, order))
                .map(order -> saveOrder(order, email))
                .map(Order::getId)
                .orElseThrow(() -> new OrderCreationException("Failed to create order"));
    }

    private Order setOrderFieldsFromInstrumentRepository(Principal principal, Order order) {
        order.setUser(principal.getName());
        List<String> instrumentIds = order.getInstrumentOrders().stream()
                .map(InstrumentOrder::getInstrumentId)
                .toList();
        log.info("Getting instruments from inventory-service for ids: {}", instrumentIds);
        Map<String, InstrumentServiceResponseDto> instrumentsMap = instrumentClient.getInventoryByInstrumentIds(
                "Bearer %s%s".formatted(environment.getProperty("api.key.instrument"),
                                environment.getProperty("application.jwt.secret")),
                        instrumentIds)
                .stream()
                .collect(Collectors.toMap(InstrumentServiceResponseDto::id, instrument -> instrument));
        log.info("Received instruments: {}", instrumentsMap);
        order.getInstrumentOrders().forEach(instrumentOrder -> {
            InstrumentServiceResponseDto instrument = instrumentsMap.get(instrumentOrder.getInstrumentId());
            if (instrument == null) {
                throw new OrderCreationException("Instrument not found");
            }
            instrumentOrder.setPrice(instrument.price());
        });
        return order;
    }

    private Order saveOrder(Order order, String email) {
        Map<String, Integer> instrumentQuantities = null;
        try {
            instrumentQuantities = order.getInstrumentOrders().stream()
                    .collect(Collectors.toMap(InstrumentOrder::getInstrumentId, InstrumentOrder::getQuantity));
            Order savedOrder = orderRepository.save(order);
            changeInstrumentQuantity(instrumentQuantities);
            List<InstrumentOrderResponseDto> instrumentOrders = instrumentOrderResponseDtoMapper.toDto(savedOrder.getInstrumentOrders())
                    .stream()
                    .map(instrumentOrderResponseDto -> new InstrumentOrderResponseDto(
                            instrumentOrderResponseDto.instrumentId(),
                            instrumentOrderResponseDto.quantity(),
                            instrumentOrderResponseDto.price()
                    ))
                    .toList();
            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getUser(),
                    savedOrder.getTitle(), email, instrumentOrders);
            log.info("Sending order created event: {}", orderCreatedEvent);
            kafkaTemplate.sendDefault(orderCreatedEvent);
            log.info("Order created successfully");
            return savedOrder;
        } catch (InventoryUpdateException e) {
            log.error("Failed to update inventory");
            throw e;
        } catch (Exception e) {
            log.error("Failed to create order");
            if (instrumentQuantities != null) {
                instrumentQuantities.replaceAll((k, v) -> -v);
                changeInstrumentQuantity(instrumentQuantities);
            }
            throw new OrderCreationException("Failed to create order");
        }
    }

    private void changeInstrumentQuantity(Map<String, Integer> instrumentQuantities) {
        try {
            inventoryClient.changeInventory("Bearer %s%s".formatted(
                    environment.getProperty("api.key.inventory"),
                    environment.getProperty("application.jwt.secret")), instrumentQuantities);
        } catch (Exception e) {
            log.error("Failed to update inventory");
            throw new InventoryUpdateException("Failed to update inventory", e);
        }
        log.info("Inventory updated successfully");
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    public OrderResponseDto getOrder(Long id, Principal principal) {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(statusRepository.findById(order.getStatusId())
                            .orElseThrow(() -> new ResourceNotFoundException("Status not found")));
                    return order;
                })
                .filter(order -> {
                    if (principal instanceof Authentication authentication) {
                        return authentication.getAuthorities().stream()
                                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
                                || order.getUser().equals(principal.getName());
                    }
                    return true;
                })
                .map(orderResponseDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private Order checkAvailability(Order order) {
        List<String> instrumentIds = order.getInstrumentOrders().stream()
                .map(InstrumentOrder::getInstrumentId)
                .toList();
        log.info("Getting inventory for instrument ids: {}", instrumentIds);
        Map<String, Integer> availabilityMap = inventoryClient.getInventoryByInstrumentIds(
                        "Bearer %s%s".formatted(environment.getProperty("api.key.inventory"),
                                environment.getProperty("application.jwt.secret")), instrumentIds);
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

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
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
            String historyOrderId = historyOrderClient.createOrderForUser(
                            "Bearer %s%s".formatted(environment.getProperty("api.key.history"),
                                    environment.getProperty("application.jwt.secret")),
                    orderHistoryCreationDto);
            log.info("Created history order with id: {}", historyOrderId);

            if (historyOrderId == null) {
                throw new ServiceNotAvailableException("Failed to create history order");
            }

            orderRepository.deleteById(id);
            return orderResponseDtoMapper.toDto(order.get());
        } else {
            return orderResponseDtoMapper.toDto(orderRepository.save(order.get()));
        }
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
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

    @PreAuthorize("hasRole('USER')")
    public Page<OrderResponseDto> getOrders(int page, int size, Principal principal) {
        log.info("Getting orders for user: {}", principal.getName());
        return orderRepository.findAllByUser(principal.getName(), PageRequest.of(page, size))
                .map(orderResponseDtoMapper::toDto);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public Page<OrderResponseDto> getOrdersForUsers(int page, int size) {
        log.info("Getting orders for users");
        return orderRepository.findAll(PageRequest.of(page, size))
                .map(orderResponseDtoMapper::toDto);
    }
}
