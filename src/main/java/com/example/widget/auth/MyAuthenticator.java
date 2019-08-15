package com.example.widget.auth;

import com.example.widget.dao.UserDao;
import com.example.widget.model.api.User;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class MyAuthenticator implements Authenticator<BasicCredentials, User> {

    private UserDao userDao;

    public MyAuthenticator(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Authenticates the credentials against a user datastore, producing the User principal object if matched.
     */
    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        User user = userDao.login(basicCredentials.getUsername(), basicCredentials.getPassword());
        if (user != null)
            return Optional.of(user);
        else
            return Optional.absent();
    }
}
