package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "user-1", "user-1@gmail.com", "123456", Role.ROLE_USER),
            new User(null, "user-2", "user-2@gmail.com", "123456", Role.ROLE_USER),
            new User(null, "user-3", "user-3@gmail.com", "123456", Role.ROLE_USER)
    );
}
