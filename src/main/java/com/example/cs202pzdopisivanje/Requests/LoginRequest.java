package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request to log in.
 */
public class LoginRequest extends Request {
    /**
     * The username of the user.
     */
    private final String username;
    /**
     * The password of the user.
     */
    private final String password;
    /**
     * The ID of the user.
     */
    private int id;

    public LoginRequest(String username, String password) {
        super();
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
