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
import com.db.store.service.interfaces.OrderServiceInterface;
import com.db.store.utils.Convertor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentOrderRepository instrumentOrderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final Convertor convertor;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        StatusRepository statusRepository,
                        InstrumentRepository instrumentRepository,
                        InstrumentOrderRepository instrumentOrderRepository,
                        OrderHistoryRepository orderHistoryRepository,
                        Convertor convertor) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.instrumentRepository = instrumentRepository;
        this.instrumentOrderRepository = instrumentOrderRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.convertor = convertor;
    }

    private static void setInstrumentOrders(Set<InstrumentOrder> instrumentOrders, Order finalOrder) {
        instrumentOrders.forEach(instrumentOrder ->
                instrumentOrder.setOrder(finalOrder));
        instrumentOrders.forEach(instrumentOrder ->
                instrumentOrder.setId(
                        new InstrumentOrderId(
                                instrumentOrder.getInstrument().getId(),
                                instrumentOrder.getOrder().getId())));
    }

    @Override
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

    private Set<InstrumentOrder> setOrderFields(Order order) {
        order.setUser(userRepository.findByLogin(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(
                        UserConstants.USER_NOT_FOUND.getMessage())));
        order.setStatus(statusRepository.findByCode(
                        StatusConstants.ORDER_PROCESSING.getMessage())
                .orElseThrow(() -> new StatusNotFoundException(
                        StatusConstants.STATUS_NOT_FOUND.getMessage())));
        order.getInstrumentOrders()
                .forEach(instrumentOrder -> {
                    instrumentOrder.setInstrument(
                            instrumentRepository.findByTitle(
                                            instrumentOrder.getInstrument().getTitle())
                                    .orElseThrow(() -> new InstrumentNotFoundException(
                                            InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage())));
                    instrumentOrder.setPrice(
                            instrumentOrder.getInstrument().getPrice());
                });
        Set<InstrumentOrder> instrumentOrders = order.getInstrumentOrders();
        order.setInstrumentOrders(instrumentOrders);
        return instrumentOrders;
    }

    @Override
    public Page<Order> getAllOrdersForLoggedUser(int page, int size) {
        User user = userRepository.findByLogin(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(
                        UserConstants.USER_NOT_FOUND.getMessage()));
        return orderRepository.findByUser(user, PageRequest.of(page, size));
    }

    @Override
    public Order getOrderForUserById(Long id) {
        User user = userRepository.findByLogin(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(
                        UserConstants.USER_NOT_FOUND.getMessage()));
        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new OrderNotFoundException(
                        OrderConstants.ORDER_NOT_FOUND.getMessage()));
    }

    @Override
    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public Order setNextStatus(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        OrderConstants.ORDER_NOT_FOUND.getMessage()));
        Status currentStatus = statusRepository.findByCode(order.getStatus().getCode())
                .orElseThrow(() -> new StatusNotFoundException(
                        StatusConstants.STATUS_NOT_FOUND.getMessage()));
        Status nextStatus = currentStatus.getNextStatuses().stream().findFirst()
                .orElseThrow(() -> new StatusNotFoundException(
                        StatusConstants.STATUS_NOT_FOUND.getMessage()));
        order.setStatus(nextStatus);
        if (nextStatus.getCode().equals(
                StatusConstants.ORDER_ARRIVED.getMessage())) {
            deleteOrderAndSaveHistoryOrder(order);
            return order;
        }
        return orderRepository.save(order);
    }

    private void deleteOrderAndSaveHistoryOrder(Order order) {
        OrderHistory orderHistory = convertor.orderToOrderHistory(order);
        instrumentOrderRepository.deleteAllByOrderId(order.getId());
        orderRepository.delete(order);
        orderHistoryRepository.save(orderHistory);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        OrderConstants.ORDER_NOT_FOUND.getMessage()));
        order.setStatus(statusRepository.findByCode(
                        StatusConstants.ORDER_CANCELED.getMessage())
                .orElseThrow(() -> new StatusNotFoundException(
                        StatusConstants.STATUS_NOT_FOUND.getMessage())));
        deleteOrderAndSaveHistoryOrder(order);
        return order;
    }

    @Override
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
        return finalOrderFromDB;
    }

    private Order getOrderFromDBAndCheckUser(Long id) {
        Order orderFromDB = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        OrderConstants.ORDER_NOT_FOUND.getMessage()));
        if (!orderFromDB.getUser().getLogin().equals(
                SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new UserNotFoundException(
                    UserConstants.USER_NOT_FOUND.getMessage());
        }
        return orderFromDB;
    }

    private Set<InstrumentOrder> updateOrderFields(Order order, Order orderFromDB) {
        orderFromDB.setTitle(order.getTitle());
        orderFromDB.setInstrumentOrders(new HashSet<>());
        order.getInstrumentOrders()
                .forEach(instrumentOrder -> {
                    instrumentOrder.setInstrument(
                            instrumentRepository.findByTitle(
                                            instrumentOrder.getInstrument().getTitle())
                                    .orElseThrow(() -> new InstrumentNotFoundException(
                                            InstrumentConstants.INSTRUMENT_NOT_FOUND.getMessage())));
                    instrumentOrder.setPrice(
                            instrumentOrder.getInstrument().getPrice());
                });
        return order.getInstrumentOrders();
    }
}
