package com.example.cs202pzdopisivanje.Requests;

/**
 * Represents the communication request for joining a group.
 */
public class JoinGroupRequest extends Request{
    private int userId;
    private String groupName;
    private String role;

    public JoinGroupRequest(int userId, String groupName, String role) {
        this.userId = userId;
        this.groupName = groupName;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
