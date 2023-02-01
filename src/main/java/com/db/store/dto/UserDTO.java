package com.db.store.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String login;

    public UserDTO() {
    }

    public UserDTO(String username) {
        this.login = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                '}';
    }
}
