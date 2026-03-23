package com.example.cs202pzdopisivanje.Requests;

public class RemoveFriendRequest extends Request {
    private String username;
    
    public RemoveFriendRequest(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
