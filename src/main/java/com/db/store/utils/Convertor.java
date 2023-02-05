package com.db.store.utils;

import com.db.store.model.Order;
import com.db.store.model.OrderHistory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class Convertor {
    public OrderHistory orderToOrderHistory(Order order) {
        BigDecimal totalSum = order.getInstrumentOrders().stream()
                .map(instrumentOrder -> instrumentOrder.getPrice().multiply(BigDecimal.valueOf(instrumentOrder.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return OrderHistory.builder()
                .setTitle("Order " + order.getTitle())
                .setDateCreated(LocalDateTime.now())
                .setTotalSum(totalSum)
                .setUser(order.getUser())
                .setStatus(order.getStatus())
                .createOrderHistory();
    }
}
