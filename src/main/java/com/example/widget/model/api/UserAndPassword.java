package com.example.widget.model.api;

import java.util.List;

/**
 * Adds a simple unencrypted password field.
 */
public class UserAndPassword extends User {

    private String password;

    public UserAndPassword() {
        super();
    }

    public UserAndPassword(String username, List<Role> roles, String password) {
        super(username, roles);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return new User(getUsername(), getRoles());
    }
}
