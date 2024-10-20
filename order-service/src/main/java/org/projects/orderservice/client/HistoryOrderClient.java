package org.projects.orderservice.client;

import org.projects.orderservice.dto.OrderHistoryCreationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "history-order-service")
public interface HistoryOrderClient {

    @RequestMapping(value = "/api/order-history", method = RequestMethod.POST)
    String createOrderForUser(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                              OrderHistoryCreationDto creationDto);
}
