package com.example.widget.model.api;

import org.apache.commons.lang3.StringUtils;

public enum Role {
    USER,
    ADMIN;

    /**
     * This exists because using plain old Role.USER or Role.USER.name() as an argument to annotations like
     * RolesAllowed gives the error "Attribute value must be constant."  However Role.Names.USER works.
     * See http://stackoverflow.com/a/16384334
     */
    public interface Names {
        String USER = "USER";
        String ADMIN = "ADMIN";
    }

    /**
     * Parses a Role enum from the input string.
     */
    public static Role fromString(String name) {
        if (StringUtils.equals(name, Names.USER))
            return USER;
        else if (StringUtils.equals(name, Names.ADMIN))
            return ADMIN;
        else
            return null;
    }
}
