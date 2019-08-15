package com.example.widget.auth;

import com.example.widget.model.api.Role;
import com.example.widget.model.api.User;
import io.dropwizard.auth.Authorizer;

public class MyAuthorizer implements Authorizer<User> {

    /**
     * For an already-authenticated user, this authorizes the user to play a particular role.
     */
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles().contains(Role.fromString(role));
    }

}
