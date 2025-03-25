package org.projects.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.projects.orderservice.dto.OrderHistoryCreationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "history-order-service", fallback = HistoryOrderClient.HistoryOrderClientFallback.class)
@CircuitBreaker(name = "default")
@Retry(name = "default")
public interface HistoryOrderClient {

    Logger log = LoggerFactory.getLogger(HistoryOrderClient.class);

    @RequestMapping(value = "/api/order-history", method = RequestMethod.POST)
    String createOrderForUser(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                              OrderHistoryCreationDto creationDto);

    @Component
    class HistoryOrderClientFallback implements HistoryOrderClient {
        @Override
        public String createOrderForUser(String authorizationHeader, OrderHistoryCreationDto creationDto) {
            log.error("Failed to create order {} for user", creationDto);
            return null;
        }
    }
}
