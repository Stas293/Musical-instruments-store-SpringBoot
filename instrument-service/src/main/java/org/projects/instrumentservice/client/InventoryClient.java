package org.projects.instrumentservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.projects.instrumentservice.exception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", fallback = InventoryClient.InventoryClientFallback.class)
@CircuitBreaker(name = "default")
@Retry(name = "default")
public interface InventoryClient {
    @RequestMapping(value = "/api/inventory/{instrumentId}", method = RequestMethod.POST)
    void setInventory(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                         @PathVariable String instrumentId, @RequestParam Integer quantity);

    @RequestMapping(value = "/api/inventory/{instrumentId}", method = RequestMethod.GET)
    Integer getInventory(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                         @PathVariable String instrumentId);

    @RequestMapping(value = "/api/inventory/{instrumentId}", method = RequestMethod.DELETE)
    void removeInventory(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                         @PathVariable String instrumentId);

    @Component
    class InventoryClientFallback implements InventoryClient {
        @Override
        public void setInventory(String authorizationHeader, String instrumentId, Integer quantity) {
            throw new ServiceNotAvailableException("Failed to set inventory");
        }

        @Override
        public Integer getInventory(String authorizationHeader, String instrumentId) {
            return 0;
        }

        @Override
        public void removeInventory(String authorizationHeader, String instrumentId) {
            throw new ServiceNotAvailableException("Failed to remove inventory");
        }
    }
}
