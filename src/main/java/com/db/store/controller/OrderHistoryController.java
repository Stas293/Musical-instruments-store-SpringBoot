package com.db.store.controller;

import com.db.store.dto.OrderHistoryDTO;
import com.db.store.service.OrderHistoryService;
import com.db.store.utils.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;
    private final ObjectMapper objectMapper;

    public OrderHistoryController(OrderHistoryService orderHistoryService,
                                  ObjectMapper objectMapper) {
        this.orderHistoryService = orderHistoryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/user/order-history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrdersForLoggedUser() {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        orderHistoryService.getAllOrderHistoriesByUser(), OrderHistoryDTO.class));
    }

    @GetMapping("/user/order-history/{id}")
    public ResponseEntity<OrderHistoryDTO> getOrderForUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderHistoryService.getAllOrderHistoriesByIdForUser(id), OrderHistoryDTO.class));
    }

    @GetMapping("/seller/order-history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrders() {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        orderHistoryService.getAllOrderHistories(), OrderHistoryDTO.class));
    }

    @GetMapping("/seller/order-history/{id}")
    public ResponseEntity<OrderHistoryDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderHistoryService.getOrderHistoryById(id), OrderHistoryDTO.class));
    }
}
