package com.db.store.model.builders;

import com.db.store.model.OrderHistory;
import com.db.store.model.Status;
import com.db.store.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderHistoryBuilder {
    private User user;
    private LocalDateTime dateCreated;
    private BigDecimal totalSum;
    private String title;
    private Status status;

    public OrderHistoryBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public OrderHistoryBuilder setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public OrderHistoryBuilder setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
        return this;
    }

    public OrderHistoryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public OrderHistoryBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public OrderHistory createOrderHistory() {
        return new OrderHistory(user, dateCreated, totalSum, title, status);
    }
}