package org.projects.instrumentservice.integration.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projects.instrumentservice.dto.InstrumentResponseDto;
import org.projects.instrumentservice.integration.IntegrationTestBase;
import org.projects.instrumentservice.integration.annotation.IT;
import org.projects.instrumentservice.model.Instrument;
import org.projects.instrumentservice.repository.InstrumentRepository;
import org.projects.instrumentservice.service.InstrumentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@IT
@RequiredArgsConstructor
class CompanyServiceIT extends IntegrationTestBase {
    private final InstrumentService instrumentService;
    private final InstrumentRepository instrumentRepository;

    @AfterEach
    void cleanUp() {
        instrumentRepository.deleteAll();
    }

    @Test
    void findById() {
        Faker faker = new Faker();

        Map<String, Instrument> instruments = IntStream.range(0, 5)
                .mapToObj(i -> Instrument.builder()
                        .id(faker.code().isbn10())
                        .title(faker.book().title())
                        .description(faker.lorem().sentence())
                        .price(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)))
                        .build())
                .collect(Collectors.toMap(
                        Instrument::getId,
                        instrument -> instrument
                ));

        instrumentRepository.saveAll(instruments.values());

        List<String> instrumentIds = instruments.values().stream()
                .map(Instrument::getId)
                .toList();

        for (String id : instrumentIds) {
            Assertions.assertDoesNotThrow(() -> instrumentService.getInstrumentById(id));
            InstrumentResponseDto instrument = instrumentService.getInstrumentById(id);

            Instrument expectedInstrument = instruments.get(id);
            Assertions.assertEquals(expectedInstrument.getId(), instrument.id());
            Assertions.assertEquals(expectedInstrument.getTitle(), instrument.title());
            Assertions.assertEquals(expectedInstrument.getDescription(), instrument.description());
            Assertions.assertEquals(expectedInstrument.getPrice(), instrument.price());
        }
    }

}
