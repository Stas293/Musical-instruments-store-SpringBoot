package org.projects.instrumentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
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
}
