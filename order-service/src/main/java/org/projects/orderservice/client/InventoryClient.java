package org.projects.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.projects.orderservice.exception.ServiceNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "inventory-service", fallback = InventoryClient.InventoryClientFallback.class)
@CircuitBreaker(name = "default")
@Retry(name = "default")
public interface InventoryClient {
    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @RequestMapping(value = "/api/inventory", method = RequestMethod.PUT)
    void changeInventory(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                         @RequestBody Map<String, Integer> instrumentIdToQuantity);

    @RequestMapping(value = "/api/inventory", method = RequestMethod.GET)
    Map<String, Integer> getInventoryByInstrumentIds(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                     @RequestParam List<String> instrumentIds);

    @Component
    class InventoryClientFallback implements InventoryClient {
        @Override
        public void changeInventory(String authorizationHeader, Map<String, Integer> instrumentIdToQuantity) {
            log.error("Failed to change inventory for instruments {}", instrumentIdToQuantity);
            throw new ServiceNotAvailableException("Inventory service is not available");
        }

        @Override
        public Map<String, Integer> getInventoryByInstrumentIds(String authorizationHeader, List<String> instrumentIds) {
            log.error("Failed to get inventory for instruments {}", instrumentIds);
            return Map.of();
        }
    }
}
