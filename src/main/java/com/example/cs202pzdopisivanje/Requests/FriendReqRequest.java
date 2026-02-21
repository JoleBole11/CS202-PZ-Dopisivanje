package com.example.cs202pzdopisivanje.Requests;

import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;

import java.util.List;

public class FriendReqRequest extends Request{
    int username;
    String type;
    private List<FriendRequestObject> friendRequests;

    public FriendReqRequest(int userId) {
        this.username = userId;
    }

    public int getUsername() {
        return username;
    }

    public List<FriendRequestObject> getFriendsRequests() {
        return friendRequests;
    }

    public void setFriends(List<FriendRequestObject> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public void setUsername(int username) {
        this.username = username;
    }
}
