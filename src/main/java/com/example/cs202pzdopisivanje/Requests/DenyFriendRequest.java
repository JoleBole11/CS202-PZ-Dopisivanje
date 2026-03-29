package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request for denying friend requests.
 */
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
