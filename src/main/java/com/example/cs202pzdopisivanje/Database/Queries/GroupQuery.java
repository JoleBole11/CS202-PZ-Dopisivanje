package com.example.cs202pzdopisivanje.Database.Queries;

public class GroupQuery {
    
    /**
     * SQL query to create a new group.
     * Inserts a new group into the chat table.
     */
    public static String createGroup() {
        return "INSERT INTO chat (chat_name, is_group, created_at) VALUES (?, ?, NOW())";
    }

    public static String createFriendChat() {
        return "INSERT INTO chat (chat_name, is_group, created_at) VALUES (?, 0, NOW())";
    }
    
    /**
     * SQL query to add a user to a group.
     * Inserts a relationship between a user and chat in the chat_member table.
     */
    public static String addUserToGroup() {
        return "INSERT INTO chat_member (user_id, chat_id, role, joined_at) " +
                "VALUES (?, (SELECT CHAT_ID FROM chat WHERE CHAT_NAME = ?), ?, NOW())";
    }

    /**
     * SQL query to check if a group name already exists.
     */
    public static String checkGroupExists() {
        return "SELECT COUNT(*) FROM chat WHERE CHAT_NAME = ?";
    }
    
    /**
     * SQL query to get all groups for a specific user.
     */
    public static String getGroups() {
        return "SELECT c.CHAT_ID, c.CHAT_NAME\n" +
            "FROM chat c\n" +
            "JOIN chat_member cm ON c.CHAT_ID = cm.CHAT_ID\n" +
            "WHERE cm.USER_ID = ? \n" +
            "AND c.IS_GROUP = 1;"; }
}