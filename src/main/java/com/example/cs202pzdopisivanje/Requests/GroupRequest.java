package com.example.cs202pzdopisivanje.Requests;

import java.util.List;

public class GroupRequest extends Request{
    int userId;
    private List<String> groups;

    public GroupRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

}
