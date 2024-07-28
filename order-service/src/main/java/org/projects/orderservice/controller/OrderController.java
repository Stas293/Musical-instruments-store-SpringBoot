package org.projects.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.orderservice.dto.OrderCreationDto;
import org.projects.orderservice.dto.OrderResponseDto;
import org.projects.orderservice.exception.ValidationException;
import org.projects.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(@RequestBody @Validated OrderCreationDto orderDto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Validation error: {}", bindingResult.getFieldErrors());
            throw new ValidationException(bindingResult.getFieldErrors().toString());
        }

        return orderService.createOrder(orderDto);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }
}
