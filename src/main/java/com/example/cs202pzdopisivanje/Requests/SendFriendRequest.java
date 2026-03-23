package com.example.cs202pzdopisivanje.Requests;

import java.util.List;

public class SendFriendRequest extends Request {
    String username;
    private List<String> friends;

    public SendFriendRequest(String username) { this.username = username;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
