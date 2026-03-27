package com.example.cs202pzdopisivanje.Requests;

import com.example.cs202pzdopisivanje.Objects.Chat;

import java.util.List;

public class GroupRequest extends Request{
    int userId;
    private List<Chat> groups;

    public GroupRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Chat> getGroups() {
        return groups;
    }

    public void setGroups(List<Chat> groups) {
        this.groups = groups;
    }

}
