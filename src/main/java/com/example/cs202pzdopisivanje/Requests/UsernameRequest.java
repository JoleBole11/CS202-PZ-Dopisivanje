package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request for getting a user.
 */
public class UsernameRequest extends Request{
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The ID of the user.
     */
    private int id;

    public UsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
