package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request for editing the user's profile.
 */
public class EditRequest extends Request{
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;


    public EditRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
