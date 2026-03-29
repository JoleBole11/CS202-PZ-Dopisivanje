package com.example.cs202pzdopisivanje.Requests;

import com.example.cs202pzdopisivanje.Objects.Chat;

import java.util.List;

/**
 * Represents the communication request for getting groups.
 */
public class GroupRequest extends Request{
    /**
     * The ID of the user.
     */
    int userId;
    /**
     * The List containing groups.
     */
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
