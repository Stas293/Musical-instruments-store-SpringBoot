package com.db.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Size(max = 255, min = 3, message = "validation.role.code.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z_]+$", message = "validation.role.code.pattern")
    @Column(name = "code", nullable = false)
    private String code;

    @Size(max = 255, min = 3, message = "validation.role.name.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "validation.role.name.pattern")
    @Column(name = "name", nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @Transient
    public String getAuthority() {
        return code;
    }
}