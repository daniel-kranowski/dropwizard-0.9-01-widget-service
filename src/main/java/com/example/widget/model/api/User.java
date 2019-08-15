package com.example.widget.model.api;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class User implements Principal {
    //Note: intentionally no password field

    private String username;

    private List<Role> roles;

    public User() {
    }

    public User(String username, List<Role> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles==null ? new ArrayList<>() : new ArrayList<>(roles);
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getName() {
        return username;
    }
}
