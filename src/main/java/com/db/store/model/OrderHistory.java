package com.db.store.model;

import com.db.store.model.builders.OrderHistoryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id", nullable = false)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @NotNull(message = "validation.order.totalSum.notNull")
    @Column(name = "total_sum", nullable = false, precision = 17, scale = 2)
    private BigDecimal totalSum;

    @Size(max = 255, min = 5, message = "validation.order.title.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "validation.order.title.pattern")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "validation.order.status.notNull")
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    public OrderHistory() {
    }

    public OrderHistory(User user, LocalDateTime dateCreated, BigDecimal totalSum, String title, Status status) {
        this.user = user;
        this.dateCreated = dateCreated;
        this.totalSum = totalSum;
        this.title = title;
        this.status = status;
    }

    public static OrderHistoryBuilder builder() {
        return new OrderHistoryBuilder();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}