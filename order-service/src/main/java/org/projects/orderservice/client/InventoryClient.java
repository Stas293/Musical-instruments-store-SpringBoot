package org.projects.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @RequestMapping(value = "/api/inventory", method = RequestMethod.PUT)
    void changeInventory(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                         @RequestBody Map<String, Integer> instrumentIdToQuantity);

    @RequestMapping(value = "/api/inventory", method = RequestMethod.GET)
    Map<String, Integer> getInventoryByInstrumentIds(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                     @RequestParam List<String> instrumentIds);
}
