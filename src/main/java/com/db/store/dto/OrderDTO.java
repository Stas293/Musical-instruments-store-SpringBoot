package com.db.store.dto;

import com.db.store.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link Order} entity
 */
public class OrderDTO implements Serializable {
    private UserDTO user;
    @Size(max = 255, min = 3, message = "validation.order.title.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.order.title.pattern")
    private String title;
    private StatusDTO status;
    private Set<InstrumentOrderDTO> instrumentOrders = new LinkedHashSet<>();

    public OrderDTO() {
    }

    public OrderDTO(UserDTO user, String title, StatusDTO status, Set<InstrumentOrderDTO> instrumentOrders) {
        this.user = user;
        this.title = title;
        this.status = status;
        this.instrumentOrders = instrumentOrders;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    public Set<InstrumentOrderDTO> getInstrumentOrders() {
        return instrumentOrders;
    }

    public void setInstrumentOrders(Set<InstrumentOrderDTO> instrumentOrders) {
        this.instrumentOrders = instrumentOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO entity = (OrderDTO) o;
        return Objects.equals(this.user, entity.user) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.instrumentOrders, entity.instrumentOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, title, status, instrumentOrders);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "user = " + user + ", " +
                "title = " + title + ", " +
                "status = " + status + ", " +
                "instrumentOrders = " + instrumentOrders + ")";
    }
}