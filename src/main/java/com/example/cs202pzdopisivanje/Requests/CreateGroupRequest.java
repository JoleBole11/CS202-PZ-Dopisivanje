package com.example.cs202pzdopisivanje.Requests;

public class CreateGroupRequest extends Request {
    private String groupName;
    private int isGroup;

    public CreateGroupRequest(String groupName, int isGroup) {
        this.groupName = groupName;
        this.isGroup = isGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }
}
