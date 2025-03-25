package org.projects.historyorderservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.historyorderservice.dto.OrderHistoryCreationDto;
import org.projects.historyorderservice.dto.OrderHistoryResponseDto;
import org.projects.historyorderservice.exception.ValidationException;
import org.projects.historyorderservice.service.OrderHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/order-history")
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderHistoryResponseDto> getOrdersForLoggedUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "5") int size,
                                                                Principal principal) {
        return orderHistoryService.getAllOrderHistories(page, size, principal);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderHistoryResponseDto> getOrdersForUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "5") int size) {
        return orderHistoryService.getAllOrderHistoriesForUser(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderHistoryResponseDto getOrderForUserById(@PathVariable String id,
                                                       Principal principal) {
        return orderHistoryService.getOrderHistoryByIdForUser(id, principal);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrderForUser(@RequestBody @Validated OrderHistoryCreationDto creationDto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors().toString());
        }

        return orderHistoryService.createOrderHistoryForUser(creationDto);
    }
}
