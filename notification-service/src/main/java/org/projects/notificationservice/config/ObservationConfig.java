package org.projects.notificationservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
@RequiredArgsConstructor
public class ObservationConfig {
    private final ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory;

    @PostConstruct
    public void init() {
        kafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
    }
}
