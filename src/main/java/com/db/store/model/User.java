package com.db.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_list")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 255, min = 4, message = "validation.text.error.login.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^\\w+$", message = "validation.text.error.login.pattern")
    @Column(name = "login", nullable = false, unique = true)
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
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 13, min = 13, message = "validation.text.error.phone.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^\\+380[0-9]{9}$", message = "validation.text.error.phone.pattern")
    @Column(name = "phone", nullable = false, length = 13, unique = true)
    private String phone;

    @Size(max = 255, min = 8, message = "validation.text.error.password.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "validation.text.error.required.field")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<OrderHistory> orderHistories = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String login) {
        this.login = login;
    }

    public User(String login, String firstName, String lastName, String email, String phone, String password, Boolean enabled) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<OrderHistory> getOrderHistories() {
        return orderHistories;
    }

    public void setOrderHistories(Set<OrderHistory> orderHistories) {
        this.orderHistories = orderHistories;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
