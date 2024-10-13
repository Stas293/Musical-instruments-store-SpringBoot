package org.projects.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.orderservice.dto.OrderCreationDto;
import org.projects.orderservice.dto.OrderResponseDto;
import org.projects.orderservice.dto.StatusResponseDto;
import org.projects.orderservice.exception.ValidationException;
import org.projects.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(Principal principal,
            @RequestBody @Validated OrderCreationDto orderDto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Validation error: {}", bindingResult.getFieldErrors());
            throw new ValidationException(bindingResult.getFieldErrors().toString());
        }

        return orderService.createOrder(orderDto, principal);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getOrder(@PathVariable Long id, Principal principal) {
        return orderService.getOrder(id, principal);
    }

    @PatchMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto updateOrder(@PathVariable Long id,
                                        @RequestParam String status) {
        return orderService.updateOrder(id, status);
    }

    @GetMapping("/{id}/nextstatuses")
    @ResponseStatus(HttpStatus.OK)
    public List<StatusResponseDto> getNextStatuses(@PathVariable Long id) {
        return orderService.getNextStatuses(id);
    }
}
