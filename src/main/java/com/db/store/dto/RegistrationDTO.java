package com.db.store.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class RegistrationDTO implements Serializable {
    @Size(max = 255, min = 4, message = "validation.text.error.login.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^\\w+$", message = "validation.text.error.login.pattern")
    @Column(name = "login", nullable = false)
    private String login;

    @Size(max = 255, min = 4, message = "validation.text.error.first.name.size")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "validation.text.error.first.name.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255, min = 4, message = "validation.text.error.last.name.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "validation.text.error.last.name.pattern")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 255, min = 4, message = "validation.text.error.email.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "validation.text.error.email.pattern")
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 13, min = 13, message = "validation.text.error.phone.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^\\+380[0-9]{9}$", message = "validation.text.error.phone.pattern")
    @Column(name = "phone", nullable = false, length = 13)
    private String phone;

    @Size(max = 255, min = 8, message = "validation.text.error.password.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Column(name = "password", nullable = false)
    private String password;

    public RegistrationDTO() {
    }

    public RegistrationDTO(String login, String firstName, String lastName, String email, String phone, String password) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
