package org.projects.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.projects.orderservice.dto.InstrumentServiceResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "instrument-service", fallback = InstrumentClient.InstrumentClientFallback.class)
@CircuitBreaker(name = "default")
@Retry(name = "default")
public interface InstrumentClient {
    Logger log = LoggerFactory.getLogger(InstrumentClient.class);

    @RequestMapping(value = "/api/instruments/inventory", method = RequestMethod.GET)
    List<InstrumentServiceResponseDto> getInventoryByInstrumentIds(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @RequestParam List<String> instrumentIds);

    @Component
    class InstrumentClientFallback implements InstrumentClient {
        @Override
        public List<InstrumentServiceResponseDto> getInventoryByInstrumentIds(String authorizationHeader,
                                                                            List<String> instrumentIds) {
            log.error("Failed to get inventory for instruments {}", instrumentIds);
            return List.of();
        }
    }
}
