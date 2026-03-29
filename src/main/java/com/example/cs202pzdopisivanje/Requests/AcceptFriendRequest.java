package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request for accepting friend requests.
 */
public class AcceptFriendRequest extends Request{
    /**
     * The username of the friend.
     */
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
