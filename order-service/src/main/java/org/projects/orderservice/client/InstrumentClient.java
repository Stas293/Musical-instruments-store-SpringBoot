package org.projects.orderservice.client;

import org.projects.orderservice.dto.InstrumentServiceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "instrument-service")
public interface InstrumentClient {

    @RequestMapping(value = "/api/instruments/inventory", method = RequestMethod.GET)
    List<InstrumentServiceResponseDto> getInventoryByInstrumentIds(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @RequestParam List<String> instrumentIds);
}
