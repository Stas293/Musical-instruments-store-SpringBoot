package com.db.store.controller;

import com.db.store.service.interfaces.OrderHistoryServiceInterface;
import com.db.store.dto.OrderHistoryDTO;
import com.db.store.utils.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderHistoryController {
    private final OrderHistoryServiceInterface orderHistoryService;
    private final ObjectMapper objectMapper;

    public OrderHistoryController(OrderHistoryServiceInterface orderHistoryService,
                                  ObjectMapper objectMapper) {
        this.orderHistoryService = orderHistoryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/user/order-history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrdersForLoggedUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                                        @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        orderHistoryService.getAllOrderHistoriesByUser(page, size).getContent(), OrderHistoryDTO.class));
    }

    @GetMapping("/user/order-history/{id}")
    public ResponseEntity<OrderHistoryDTO> getOrderForUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderHistoryService.getOrderHistoryByIdForUser(id), OrderHistoryDTO.class));
    }

    @GetMapping("/seller/order-history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrderHistories(@RequestParam(required = false, defaultValue = "0") int page,
                                                                   @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ok(
                objectMapper.mapList(
                        orderHistoryService.getAllOrderHistories(page, size).getContent(), OrderHistoryDTO.class));
    }

    @GetMapping("/seller/order-history/{id}")
    public ResponseEntity<OrderHistoryDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(
                objectMapper.map(
                        orderHistoryService.getOrderHistoryById(id), OrderHistoryDTO.class));
    }
}
