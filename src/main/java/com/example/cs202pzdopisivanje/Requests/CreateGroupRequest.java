package com.example.cs202pzdopisivanje.Requests;

public class CreateGroupRequest extends Request {
    private int userId;
    private String groupName;

    public CreateGroupRequest(int userId, String groupName) {
        this.userId = userId;
        this.groupName = groupName;
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
}
