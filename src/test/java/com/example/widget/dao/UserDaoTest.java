package com.example.widget.dao;

import com.example.widget.model.api.Role;
import com.example.widget.model.api.User;
import com.example.widget.model.api.UserAndPassword;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public class UserDaoTest {

    public static final String USERNAME_FRED = "fred";
    public static final String PASSWORD_FRED = "abc";

    public static final String USERNAME_RIDLEY = "ridley";

    private UserDao userDao;

    @Before
    public void setup() {
        HashMap<String, UserAndPassword> initialUsers = new HashMap<>();
        initialUsers.put(USERNAME_FRED, new UserAndPassword(USERNAME_FRED, Arrays.asList(Role.USER), PASSWORD_FRED));
        userDao = new UserDao(initialUsers);
    }

    @Test
    public void usernameExists() {
        assertTrue(userDao.usernameExists(USERNAME_FRED));
        assertFalse(userDao.usernameExists(USERNAME_RIDLEY));
    }

    @Test
    public void loginPass() {
        User user = userDao.login(USERNAME_FRED, PASSWORD_FRED);
        assertTrue(user.getRoles().contains(Role.USER));
    }

    @Test
    public void loginFail() {
        assertNull(userDao.login(USERNAME_RIDLEY, PASSWORD_FRED));
    }
}