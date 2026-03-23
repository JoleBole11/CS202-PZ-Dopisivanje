package com.example.cs202pzdopisivanje.Requests;

public class AcceptFriendRequest extends Request{
    private String username;

    public AcceptFriendRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
