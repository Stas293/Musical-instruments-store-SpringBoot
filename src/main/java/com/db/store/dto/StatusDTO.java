package com.db.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.db.store.model.Status} entity
 */
public class StatusDTO implements Serializable {
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z_]+$", message = "validation.text.error.code.pattern")
    @Size(max = 255, min = 3, message = "validation.text.error.code.size")
    private String code;
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.text.error.name.pattern")
    @Size(max = 255, min = 3, message = "validation.text.error.name.size")
    private String name;
    @NotNull(message = "validation.text.error.required.field")
    private Boolean closed;

    public StatusDTO() {
    }

    public StatusDTO(String code, String name, Boolean closed) {
        this.code = code;
        this.name = name;
        this.closed = closed;
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

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusDTO entity = (StatusDTO) o;
        return Objects.equals(this.code, entity.code) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.closed, entity.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, closed);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "code = " + code + ", " +
                "name = " + name + ", " +
                "closed = " + closed + ")";
    }
}