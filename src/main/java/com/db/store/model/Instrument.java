package com.db.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "instrument_list")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrument_id", nullable = false)
    private Long id;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @Size(max = 255, min = 3, message = "validation.instrument.description.size")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.instrument.description.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 255, min = 3, message = "validation.instrument.title.size")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.instrument.title.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "validation.instrument.status.notNull")
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @NotNull(message = "validation.instrument.price.notNull")
    @Column(name = "price", nullable = false, precision = 17, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "instrument")
    private Set<InstrumentOrder> instrumentOrders = new LinkedHashSet<>();

    public Set<InstrumentOrder> getInstrumentOrders() {
        return instrumentOrders;
    }

    public void setInstrumentOrders(Set<InstrumentOrder> instrumentOrders) {
        this.instrumentOrders = instrumentOrders;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
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
}
