package com.example.cs202pzdopisivanje.Database.Queries;

/**
 * FriendQuery holds all the SQL Queries as strings related to friends.
 */
public class FriendQuery {
    /**
     * SQL query to get friends of the user.
     */
    public static String getFriends() {
        return "SELECT u.USERNAME, c.CHAT_ID, c.CHAT_NAME " +
                "FROM user u " +
                "JOIN friends f ON (u.USER_ID = f.FRIEND_ID OR u.USER_ID = f.USER_ID) " +
                "LEFT JOIN chat c ON (c.CHAT_NAME = CONCAT((SELECT USERNAME FROM user WHERE USER_ID = ?), u.USERNAME) " +
                "                     OR c.CHAT_NAME = CONCAT(u.USERNAME, (SELECT USERNAME FROM user WHERE USER_ID = ?))) " +
                "WHERE f.USER_ID = ? " +
                "  AND u.USER_ID != ? " +
                "  AND f.STATUS = 'Accepted' " +
                "  AND c.IS_GROUP = 0;";
    }

    /**
     * SQL query to get friend requests of the user.
     */
    public static String getRequests() {
        return "SELECT u.USERNAME, f.STATUS " +
                "FROM user u " +
                "JOIN friends f ON (u.USER_ID = f.FRIEND_ID OR u.USER_ID = f.USER_ID) " +
                "WHERE f.USER_ID = ? " +
                "  AND u.USER_ID != ? " +
                "  AND (f.STATUS = 'Pending' OR f.STATUS = 'Sent');";
    }

    /**
     * SQL query to send a friend request.
     */
    public static String sendFriendRequest() {
        return "INSERT INTO friends (USER_ID, FRIEND_ID, STATUS) " +
                "VALUES " +
                "(?, ?, 'Sent'), " +
                "(?, ?, 'Pending')";
    }

    /**
     * SQL query to accept a friend request.
     */
    public static String acceptFriendRequest() {
        return "UPDATE friends SET STATUS = 'Accepted' " +
                "WHERE ((USER_ID = ? AND FRIEND_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?)) " +
                "OR (USER_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?) AND FRIEND_ID = ?)) " +
                "AND STATUS IN ('Pending', 'Sent')";
    }

    /**
     * SQL query to deny a friend request.
     */
    public static String denyFriendRequest() {
        return "DELETE FROM friends " +
                "WHERE ((USER_ID = ? AND FRIEND_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?)) " +
                "OR (USER_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?) AND FRIEND_ID = ?)) ";
    }

    /**
     * SQL query to remove a friend.
     */
    public static String removeFriend() {
        return "DELETE FROM friends " +
                "WHERE ((USER_ID = ? AND FRIEND_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?)) " +
                "OR (USER_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?) AND FRIEND_ID = ?)) " +
                "AND STATUS = 'Accepted'";
    }

    /**
     * SQL query to get a user's ID by their username.
     */
    public static String getIDByUsername(){
        return "SELECT user_id FROM user WHERE USERNAME = ?";
    }

    /**
     * SQL query to check if a friend relationship between 2 users exists.
     */
    public static String checkRelationship() {
        return "SELECT COUNT(*) FROM friends WHERE " +
                "(USER_ID = ? AND FRIEND_ID = ?) OR (USER_ID = ? AND FRIEND_ID = ?)";
    }

    /**
     * SQL query to get a user's username by their ID.
     */
    public static String getUsernameByID() {
        return "SELECT USERNAME FROM user WHERE USER_ID = ?";
    }

    /**
     * SQL query to insert a user into a friend chat.
     */
    public static String insertUserIntoFriends() {
        return "INSERT INTO chat_members (CHAT_ID, USER_ID) VALUES (?, ?)";
    }

    /**
     * SQL query to get a Chat ID by its name.
     */
    public static String getChatIDByName() {
        return "SELECT CHAT_ID FROM chat WHERE CHAT_NAME = ?";
    }

    /**
     * SQL query to delete a chat by its ID.
     */
    public static String deleteChatByID() {
        return "DELETE FROM chat WHERE CHAT_ID = ?";
    }
}