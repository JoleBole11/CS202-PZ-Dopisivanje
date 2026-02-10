package com.example.cs202pzdopisivanje.Requests;

import com.example.cs202pzdopisivanje.Objects.User;

/**
 * UserRequest class represents a request for retrieving user information associated
 * with a specific user account.
 */
public class UserRequest extends Request {
    private int id;
    private User user = null;

    public UserRequest(int id) {
        super();
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(String username, String password) {
        this.user = new User(username, password);
    }

    public int getId() {
        return id;
    }
}