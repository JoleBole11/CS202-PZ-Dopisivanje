package com.example.cs202pzdopisivanje.Requests;

public class DenyFriendRequest extends Request {
    private String username;
    
    public DenyFriendRequest(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
