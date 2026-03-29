package com.example.cs202pzdopisivanje.Database.Queries;

public class FriendQuery {
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

    public static String getRequests() {
        return "SELECT u.USERNAME, f.STATUS " +
                "FROM user u " +
                "JOIN friends f ON (u.USER_ID = f.FRIEND_ID OR u.USER_ID = f.USER_ID) " +
                "WHERE f.USER_ID = ? " +
                "  AND u.USER_ID != ? " +
                "  AND (f.STATUS = 'Pending' OR f.STATUS = 'Sent');";
    }

    public static String sendFriendRequest() {
        return "INSERT INTO friends (USER_ID, FRIEND_ID, STATUS) " +
        "VALUES " +
                "(?, ?, 'Sent'), " +
                "(?, ?, 'Pending')";
    }

    public static String checkFriendRequest() {
        return "SELECT COUNT(*) FROM friends f " +
                "JOIN user u ON (f.FRIEND_ID = u.USER_ID OR f.USER_ID = u.USER_ID) " +
                "WHERE ((f.USER_ID = ? AND u.USERNAME = ?) OR " +
                "(f.FRIEND_ID = ? AND u.USERNAME = ?)) " +
                "AND u.USER_ID != ?";
    }

    public static String acceptFriendRequest() {
        return "UPDATE friends SET STATUS = 'Accepted' " +
                "WHERE ((USER_ID = ? AND FRIEND_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?)) " +
                "OR (USER_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?) AND FRIEND_ID = ?)) " +
                "AND STATUS IN ('Pending', 'Sent')";
    }

    public static String denyFriendRequest(){
        return "DELETE FROM friends " +
                "WHERE ((USER_ID = ? AND FRIEND_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?)) " +
                "OR (USER_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?) AND FRIEND_ID = ?)) ";
    }

    public static String removeFriend(){
        return "DELETE FROM friends " +
                "WHERE ((USER_ID = ? AND FRIEND_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?)) " +
                "OR (USER_ID = (SELECT USER_ID FROM user WHERE USERNAME = ?) AND FRIEND_ID = ?)) " +
                "AND STATUS = 'Accepted'";
    }
}