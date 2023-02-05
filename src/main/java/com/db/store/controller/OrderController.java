package com.db.store.controller;

import com.db.store.dto.OrderDTO;
import com.db.store.model.Order;
import com.db.store.service.interfaces.OrderServiceInterface;
import com.db.store.utils.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    private final OrderServiceInterface orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderController(OrderServiceInterface orderService,
                           ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/user/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        Order order = objectMapper.map(orderDTO, Order.class);
        return ResponseEntity.ok(
                objectMapper.map(
                        orderService.create(order), OrderDTO.class));
    }

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersForLoggedUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                                 @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        orderService.getAllOrdersForLoggedUser(page, size).getContent(), OrderDTO.class));
    }

    @GetMapping("/user/orders/{id}")
    public ResponseEntity<OrderDTO> getOrderForUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderService.getOrderForUserById(id), OrderDTO.class));
    }

    @GetMapping("/seller/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        orderService.getAllOrders(page, size).getContent(), OrderDTO.class));
    }

    @PatchMapping("/seller/orders/{id}")
    public ResponseEntity<OrderDTO> setNextStatus(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderService.setNextStatus(id), OrderDTO.class));
    }

    @PatchMapping("/seller/orders/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderService.cancelOrder(id), OrderDTO.class));
    }

    @PutMapping("/user/orders/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id,
                                                @RequestBody @Valid OrderDTO orderDTO) {
        Order order = objectMapper.map(orderDTO, Order.class);
        return ResponseEntity.ok(
                objectMapper.map(
                        orderService.updateOrder(id, order), OrderDTO.class));
    }
}
