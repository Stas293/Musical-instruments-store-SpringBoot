package org.projects.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.orderservice.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-topic")
    public void consumeOrder(OrderCreatedEvent orderCreatedEvent) {
        log.info("Consumed order: {}", orderCreatedEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("instrumentsshop@gmail.com");
            messageHelper.setTo(orderCreatedEvent.getEmail());
            messageHelper.setSubject("The order for %s has been created".formatted(orderCreatedEvent.getTitle()));
            messageHelper.setText(
                    """
                            Dear %s, your order has been created.\s
                           \s
                            The details are: %s.\s
                            Order ID: %d.\s
                            Total price: %s
                           \s
                            Thank you for your order!
                           \s""".formatted(
                    orderCreatedEvent.getUser(),
                    orderCreatedEvent.getInstrumentOrders().stream()
                            .map(instrumentOrderResponseDto -> "Instrument: %s, Quantity: %d, Price: %s".formatted(
                                    instrumentOrderResponseDto.getInstrumentId(),
                                    instrumentOrderResponseDto.getQuantity(),
                                    instrumentOrderResponseDto.getPrice()
                            ))
                            .reduce("", (s1, s2) -> s1 + "\n" + s2),
                    orderCreatedEvent.getId(),
                    orderCreatedEvent.getInstrumentOrders().stream()
                            .map(instrumentOrderResponseDto -> instrumentOrderResponseDto.getPrice() *
                                    instrumentOrderResponseDto.getQuantity())
                            .reduce(0.0, Double::sum)
            ));
        };

        javaMailSender.send(messagePreparator);
        log.info("Email sent to: {}", orderCreatedEvent.getEmail());
    }
}
