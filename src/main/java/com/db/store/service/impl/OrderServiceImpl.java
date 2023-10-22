package com.db.store.service.impl;

import com.db.store.constants.InstrumentConstants;
import com.db.store.dto.OrderDTO;
import com.db.store.exceptions.InstrumentNotFoundException;
import com.db.store.exceptions.OrderNotFoundException;
import com.db.store.exceptions.StatusNotFoundException;
import com.db.store.exceptions.UserNotFoundException;
import com.db.store.mapper.OrderDtoMapper;
import com.db.store.model.*;
import com.db.store.repository.*;
import com.db.store.service.OrderService;
import com.db.store.utils.Convertor;
import com.db.store.utils.ExceptionParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.db.store.constants.OrderConstants.ORDER_NOT_FOUND;
import static com.db.store.constants.StatusConstants.*;
import static com.db.store.constants.UserConstants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentOrderRepository instrumentOrderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final Convertor convertor;
    private final OrderDtoMapper orderDtoMapper;

    private static void setInstrumentOrders(List<InstrumentOrder> instrumentOrders, Order finalOrder) {
        instrumentOrders.forEach(instrumentOrder -> instrumentOrder.setOrder(finalOrder));
        instrumentOrders.forEach(instrumentOrder ->
                instrumentOrder.setId(
                        new InstrumentOrderId(
                                instrumentOrder.getInstrument().getId(),
                                instrumentOrder.getOrder().getId())));
    }

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
        Order order = orderDtoMapper.toEntity(orderDTO);
        List<InstrumentOrder> instrumentOrders = setOrderFields(order);
        order = orderRepository.save(order);
        Order finalOrder = order;
        setInstrumentOrders(instrumentOrders, finalOrder);
        instrumentOrderRepository.saveAll(instrumentOrders);
        return orderDtoMapper.toDto(order);
    }

    private List<InstrumentOrder> setOrderFields(Order order) {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(loggedUser)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        Status status = statusRepository.findByCode(ORDER_PROCESSING.getMessage())
                .orElseThrow(() -> new StatusNotFoundException(STATUS_NOT_FOUND.getMessage()));
        List<String> instrumentTitles = order.getInstrumentOrders()
                .stream()
                .map(instrumentOrder -> instrumentOrder.getInstrument().getTitle())
                .toList();

        Map<String, Instrument> instruments = instrumentRepository
                .findByTitleIn(instrumentTitles)
                .stream()
                .collect(Collectors.toMap(Instrument::getTitle, instrument -> instrument));

        order.setUser(user);
        order.setStatus(status);
        order.getInstrumentOrders()
                .forEach(instrumentOrder -> {
                    instrumentOrder.setInstrument(
                            instruments.get(instrumentOrder.getInstrument().getTitle()));
                    instrumentOrder.setPrice(
                            instrumentOrder.getInstrument().getPrice());
                });
        List<InstrumentOrder> instrumentOrders = order.getInstrumentOrders();
        order.setInstrumentOrders(instrumentOrders);
        return instrumentOrders;
    }

    @Override
    public Page<OrderDTO> getAllOrdersForLoggedUser(int page, int size) {
        User user = userRepository.findByLogin(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(ExceptionParser.exceptionSupplier(
                        UserNotFoundException.class,
                        USER_NOT_FOUND.getMessage())
                );
        return orderRepository.findByUser(user, PageRequest.of(page, size))
                .map(orderDtoMapper::toDto);
    }

    @Override
    public OrderDTO getOrderForUserById(Long id) {
        User user = userRepository.findByLogin(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        return orderRepository.findByIdAndUser(id, user)
                .map(orderDtoMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.getMessage()));
    }

    @Override
    public Page<OrderDTO> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size))
                .map(orderDtoMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDTO setNextStatus(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.getMessage()));
        Status currentStatus = statusRepository.findByCode(order.getStatus().getCode())
                .orElseThrow(() -> new StatusNotFoundException(STATUS_NOT_FOUND.getMessage()));
        Status nextStatus = currentStatus.getNextStatuses().stream().findFirst()
                .orElseThrow(() -> new StatusNotFoundException(STATUS_NOT_FOUND.getMessage()));
        order.setStatus(nextStatus);
        if (nextStatus.getCode().equals(ORDER_ARRIVED.getMessage())) {
            deleteOrderAndSaveHistoryOrder(order);
            return orderDtoMapper.toDto(order);
        }
        return orderDtoMapper.toDto(orderRepository.save(order));
    }

    private void deleteOrderAndSaveHistoryOrder(Order order) {
        OrderHistory orderHistory = convertor.orderToOrderHistory(order);
        instrumentOrderRepository.deleteAllByOrderId(order.getId());
        orderRepository.delete(order);
        orderHistoryRepository.save(orderHistory);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.getMessage()));
        order.setStatus(statusRepository.findByCode(ORDER_CANCELED.getMessage())
                .orElseThrow(() -> new StatusNotFoundException(STATUS_NOT_FOUND.getMessage())));
        deleteOrderAndSaveHistoryOrder(order);
        return orderDtoMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order orderFromDB = getOrderFromDBAndCheckUser(id);
        instrumentOrderRepository.deleteAllByOrderId(orderFromDB.getId());
        Order order = orderDtoMapper.toEntity(orderDTO);
        List<InstrumentOrder> instrumentOrders = updateOrderFields(order, orderFromDB);
        orderFromDB = orderRepository.save(orderFromDB);
        orderFromDB.setInstrumentOrders(instrumentOrders);
        Order finalOrderFromDB = orderFromDB;
        setInstrumentOrders(instrumentOrders, finalOrderFromDB);
        instrumentOrderRepository.saveAll(instrumentOrders);
        return orderDtoMapper.toDto(finalOrderFromDB);
    }

    private Order getOrderFromDBAndCheckUser(Long id) {
        Order orderFromDB = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.getMessage()));
        if (!orderFromDB.getUser().getLogin().equals(
                SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new UserNotFoundException(USER_NOT_FOUND.getMessage());
        }
        return orderFromDB;
    }

    private List<InstrumentOrder> updateOrderFields(Order order, Order orderFromDB) {
        orderFromDB.setTitle(order.getTitle());
        orderFromDB.setInstrumentOrders(new ArrayList<>());
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
