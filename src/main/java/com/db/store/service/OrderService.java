package com.db.store.service;

import com.db.store.constants.InstrumentConstants;
import com.db.store.constants.OrderConstants;
import com.db.store.constants.StatusConstants;
import com.db.store.constants.UserConstants;
import com.db.store.exceptions.InstrumentNotFoundException;
import com.db.store.exceptions.OrderNotFoundException;
import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.exceptions.UserNotFoundException;
import com.db.store.model.*;
import com.db.store.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentOrderRepository instrumentOrderRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        StatusRepository statusRepository,
                        InstrumentRepository instrumentRepository,
                        InstrumentOrderRepository instrumentOrderRepository,
                        OrderHistoryRepository orderHistoryRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.instrumentRepository = instrumentRepository;
        this.instrumentOrderRepository = instrumentOrderRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Transactional
    public Order create(Order order) {
        order.setDateCreated(LocalDateTime.now());
        Set<InstrumentOrder> instrumentOrders = setOrderFields(order);
        order = orderRepository.save(order);
        Order finalOrder = order;
        setInstrumentOrders(instrumentOrders, finalOrder);
        instrumentOrderRepository.saveAll(instrumentOrders);
        return order;
    }

    private static void setInstrumentOrders(Set<InstrumentOrder> instrumentOrders, Order finalOrder) {
        instrumentOrders.forEach(instrumentOrder -> instrumentOrder.setOrder(finalOrder));
        instrumentOrders.forEach(instrumentOrder -> instrumentOrder.setId(
                new InstrumentOrderId(
                        instrumentOrder.getInstrument().getId(),
                        instrumentOrder.getOrder().getId())));
    }

    private Set<InstrumentOrder> setOrderFields(Order order) {
        order.setUser(userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage())));
        order.setStatus(statusRepository.findByCode(StatusConstants.ORDER_PROCESSING.getMessage())
                .orElseThrow(() -> new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage())));
        Set<InstrumentOrder> instrumentOrders = order.getInstrumentOrders()
                .stream().peek(instrumentOrder -> {
                    instrumentOrder.setInstrument(
                            instrumentRepository.findByTitle(instrumentOrder.getInstrument().getTitle())
                                    .orElseThrow(() -> new InstrumentNotFoundException(InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage())));
                    instrumentOrder.setPrice(instrumentOrder.getInstrument().getPrice());
                })
                .collect(Collectors.toSet());
        order.setInstrumentOrders(instrumentOrders);
        return instrumentOrders;
    }

    public Page<Order> getAllOrdersForLoggedUser(int page, int size) {
        User user = userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        return orderRepository.findByUser(user, PageRequest.of(page, size));
    }

    public Order getOrderForUserById(Long id) {
        User user = userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new OrderNotFoundException(OrderConstants.ORDER_NOT_FOUND.getMessage()));
    }

    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public Order setNextStatus(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(OrderConstants.ORDER_NOT_FOUND.getMessage()));
        Status currentStatus = statusRepository.findByCode(order.getStatus().getCode())
                .orElseThrow(() -> new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage()));
        Status nextStatus = currentStatus.getNextStatuses().stream().findFirst()
                .orElseThrow(() -> new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage()));
        order.setStatus(nextStatus);
        if (nextStatus.getCode().equals(StatusConstants.ORDER_ARRIVED.getMessage())) {
            collectHistoryOrder(order, nextStatus);
            return order;
        }
        return orderRepository.save(order);
    }

    private void collectHistoryOrder(Order order, Status nextStatus) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setTitle("Order " + order.getTitle());
        orderHistory.setDateCreated(LocalDateTime.now());
        BigDecimal totalSum = order.getInstrumentOrders().stream()
                .map(instrumentOrder -> instrumentOrder.getPrice().multiply(BigDecimal.valueOf(instrumentOrder.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderHistory.setTotalSum(totalSum);
        orderHistory.setUser(order.getUser());
        orderHistory.setStatus(nextStatus);
        instrumentOrderRepository.deleteAllByOrderId(order.getId());
        orderRepository.delete(order);
        orderHistoryRepository.save(orderHistory);
    }

    @Transactional
    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(OrderConstants.ORDER_NOT_FOUND.getMessage()));
        order.setStatus(statusRepository.findByCode(StatusConstants.ORDER_CANCELED.getMessage())
                .orElseThrow(() -> new StatusNotFoundException(StatusConstants.STATUS_NOT_FOUND.getMessage())));
        collectHistoryOrder(order, order.getStatus());
        return order;
    }

    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order orderFromDB = getOrderFromDBAndCheckUser(id);
        instrumentOrderRepository.deleteAllByOrderId(orderFromDB.getId());
        Set<InstrumentOrder> instrumentOrders = updateOrderFields(order, orderFromDB);
        orderFromDB = orderRepository.save(orderFromDB);
        orderFromDB.setInstrumentOrders(instrumentOrders);
        Order finalOrderFromDB = orderFromDB;
        setInstrumentOrders(instrumentOrders, finalOrderFromDB);
        instrumentOrderRepository.saveAll(instrumentOrders);
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(OrderConstants.ORDER_NOT_FOUND.getMessage()));
    }

    private Order getOrderFromDBAndCheckUser(Long id) {
        Order orderFromDB = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(OrderConstants.ORDER_NOT_FOUND.getMessage()));
        if (!orderFromDB.getUser().getLogin().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage());
        }
        return orderFromDB;
    }

    private Set<InstrumentOrder> updateOrderFields(Order order, Order orderFromDB) {
        orderFromDB.setTitle(order.getTitle());
        orderFromDB.setInstrumentOrders(new HashSet<>());
        return order.getInstrumentOrders()
                .stream().peek(instrumentOrder -> {
                    instrumentOrder.setInstrument(
                            instrumentRepository.findByTitle(instrumentOrder.getInstrument().getTitle())
                                    .orElseThrow(() -> new InstrumentNotFoundException(InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage())));
                    instrumentOrder.setPrice(instrumentOrder.getInstrument().getPrice());
                })
                .collect(Collectors.toSet());
    }
}
