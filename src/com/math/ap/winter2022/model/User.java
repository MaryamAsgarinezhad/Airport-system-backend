package com.math.ap.winter2022.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class User {
    public List<String> allUsernames=new ArrayList<>();
    public List<User> allUsers=new ArrayList<>();
    protected String username;
    protected String password;
    protected User(String username, String password) {
        this.username=username;
        this.password=password;
        allUsernames.add(username);
        allUsers.add(this);
    }

    public List<String> getAllUsernames() {
        return allUsernames;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
