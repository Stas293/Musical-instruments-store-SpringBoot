package com.db.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "order_list")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Size(max = 255, min = 3, message = "validation.order.title.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.order.title.pattern")
    @Column(name = "title", nullable = false)
    private String title;
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @NotNull(message = "validation.order.closed.notNull")
    @Column(name = "closed", nullable = false)
    private Boolean closed = false;

    @OneToMany(mappedBy = "order")
    private Set<InstrumentOrder> instrumentOrders = new LinkedHashSet<>();

    public Set<InstrumentOrder> getInstrumentOrders() {
        return instrumentOrders;
    }

    public void setInstrumentOrders(Set<InstrumentOrder> instrumentOrders) {
        this.instrumentOrders = instrumentOrders;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
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