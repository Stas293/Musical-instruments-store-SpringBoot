package com.db.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.db.store.model.Role} entity
 */
public class RoleDTO implements Serializable {
    @Size(max = 255, min = 3, message = "validation.role.code.size")
    @NotBlank(message = "validation.role.code.required")
    @Pattern(regexp = "^[A-Z]+$", message = "validation.role.code.pattern")
    private String code;
    @Size(max = 255, min = 3, message = "validation.role.name.size")
    @NotBlank(message = "validation.role.name.required")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "validation.role.name.pattern")
    private String name;

    public RoleDTO() {
    }

    public RoleDTO(String code, String name) {
        this.code = code;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO entity = (RoleDTO) o;
        return Objects.equals(this.code, entity.code) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "code = " + code + ", " +
                "name = " + name + ")";
    }
}