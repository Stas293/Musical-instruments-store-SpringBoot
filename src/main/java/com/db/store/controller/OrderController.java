package com.db.store.controller;

import com.db.store.dto.OrderDTO;
import com.db.store.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/user/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody @Validated OrderDTO orderDTO,
                                BindingResult bindingResult) {
        return orderService.create(orderDTO, bindingResult);
    }

    @GetMapping("/user/orders")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderDTO> getOrdersForLoggedUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "5") int size) {
        return orderService.getAllOrdersForLoggedUser(page, size);
    }

    @GetMapping("/user/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderForUserById(@PathVariable Long id) {
        return orderService.getOrderForUserById(id);
    }

    @GetMapping("/seller/orders")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderDTO> getOrders(@RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "5") int size) {
        return orderService.getAllOrders(page, size);
    }

    @PatchMapping("/seller/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO setNextStatus(@PathVariable Long id) {
        return orderService.setNextStatus(id);
    }

    @PatchMapping("/seller/orders/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @PutMapping("/user/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO updateOrder(@PathVariable Long id,
                                @RequestBody @Validated OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO);
    }
}
