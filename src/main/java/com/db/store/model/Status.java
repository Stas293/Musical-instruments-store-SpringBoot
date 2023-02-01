package com.db.store.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @Column(name = "status_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z_]+$", message = "validation.text.error.code.pattern")
    @Size(max = 255, min = 3, message = "validation.text.error.code.size")
    @Column(name = "code", unique = true)
    private String code;

    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.text.error.name.pattern")
    @Size(max = 255, min = 3, message = "validation.text.error.name.size")
    @Column(name = "name", unique = true)
    private String name;

    @NotNull(message = "validation.text.error.required.field")
    @Column(name = "closed")
    private Boolean closed;

    @ManyToMany
    @JoinTable(name = "next_status",
            joinColumns = @JoinColumn(name = "status_id"),
            inverseJoinColumns = @JoinColumn(name = "next_status_id"))
    private Set<Status> nextStatuses = new LinkedHashSet<>();

    public Set<Status> getNextStatuses() {
        return nextStatuses;
    }

    public void setNextStatuses(Set<Status> nextStatuses) {
        this.nextStatuses = nextStatuses;
    }

    public Status() {
    }

    public Status(Long id, String code, String name, Boolean closed) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.closed = closed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", isClosed=" + closed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return id.equals(status.id) && code.equals(status.code) && name.equals(status.name) && closed.equals(status.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, closed);
    }
}
