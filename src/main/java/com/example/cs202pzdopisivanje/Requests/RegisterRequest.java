package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request for registering a new profile.
 */
public class RegisterRequest extends Request{
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;
    /**
     * The ID of the user.
     */
    private int id;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
