package com.example.cs202pzdopisivanje.Requests;

import com.example.cs202pzdopisivanje.Objects.Chat;

import java.util.List;

/**
 * Represents the communication request for getting a user's friends.
 */
public class FriendRequest extends Request{
    int username;
    private List<Chat> friends;

    public FriendRequest(int userId) {
        this.username = userId;
    }

    public int getUsername() {
        return username;
    }

    public List<Chat> getFriends() {
        return friends;
    }

    public void setFriends(List<Chat> friends) {
        this.friends = friends;
    }

    public void setUsername(int username) {
        this.username = username;
    }
}