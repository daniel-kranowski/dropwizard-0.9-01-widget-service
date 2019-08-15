package com.example.widget.dao;

import com.example.widget.model.api.User;
import com.example.widget.model.api.UserAndPassword;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stub dao - an in-memory collection of users.  No password encryption.
 */
public class UserDao {
    private Map<String, UserAndPassword> usersAndPasswords;

    public UserDao(Map<String, UserAndPassword> initialUsers) {
        if (initialUsers == null) {
            this.usersAndPasswords = new HashMap<>();
        }
        else {
            this.usersAndPasswords = initialUsers;
        }
    }

    /**
     * Returns true if the username is known.
     */
    public boolean usernameExists(String username) {
        return username != null && usersAndPasswords.containsKey(username);
    }

    /**
     * Checks the credentials, and if passed then returns the User, else null.
     */
    public User login(String username, String password) {
        if (usernameExists(username) && password != null) {
            UserAndPassword userAndPassword = usersAndPasswords.get(username);
            if (StringUtils.equals(userAndPassword.getPassword(), password)) {
                return userAndPassword.getUser();
            }
        }
        return null;
    }
}
