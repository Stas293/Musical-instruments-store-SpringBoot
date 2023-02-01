package com.db.store.dto;

import com.db.store.model.OrderHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link OrderHistory} entity
 */
public class OrderHistoryDTO implements Serializable {
    private UserDTO user;
    @NotNull(message = "validation.order.totalSum.notNull")
    private BigDecimal totalSum;
    @Size(max = 255, min = 5, message = "validation.order.title.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "validation.order.title.pattern")
    private String title;
    @NotNull(message = "validation.order.status.notNull")
    private StatusDTO status;

    public OrderHistoryDTO() {
    }

    public OrderHistoryDTO(UserDTO user, BigDecimal totalSum, String title, StatusDTO status) {
        this.user = user;
        this.totalSum = totalSum;
        this.title = title;
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderHistoryDTO entity = (OrderHistoryDTO) o;
        return Objects.equals(this.user, entity.user) &&
                Objects.equals(this.totalSum, entity.totalSum) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.status, entity.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, totalSum, title, status);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "user = " + user + ", " +
                "totalSum = " + totalSum + ", " +
                "title = " + title + ", " +
                "status = " + status + ")";
    }
}