package com.db.store.controller;

import com.db.store.dto.OrderHistoryDTO;
import com.db.store.service.OrderHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    @GetMapping("/user/order-history")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderHistoryDTO> getOrdersForLoggedUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "5") int size) {
        return orderHistoryService.getAllOrderHistoriesByUser(page, size);
    }

    @GetMapping("/user/order-history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderHistoryDTO getOrderForUserById(@PathVariable Long id) {
        return orderHistoryService.getOrderHistoryByIdForUser(id);
    }

    @GetMapping("/seller/order-history")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderHistoryDTO> getOrderHistories(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "5") int size) {
        return orderHistoryService.getAllOrderHistories(page, size);
    }

    @GetMapping("/seller/order-history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderHistoryDTO getOrderById(@PathVariable Long id) {
        return orderHistoryService.getOrderHistoryById(id);
    }
}
