package com.example.cs202pzdopisivanje.Requests;

import java.util.List;

public class FriendRequest extends Request{
    int username;
    private List<String> friends;

    public FriendRequest(int userId) {
        this.username = userId;
    }

    public int getUsername() {
        return username;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void setUsername(int username) {
        this.username = username;
    }
}
