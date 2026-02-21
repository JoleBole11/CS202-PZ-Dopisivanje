package com.example.cs202pzdopisivanje.Database.Queries;

public class FriendQuery {
    public static String getFriends() {
        return "SELECT u.USERNAME\n" +
                "FROM user u\n" +
                "JOIN friends f ON (u.USER_ID = f.FRIEND_ID OR u.USER_ID = f.USER_ID)\n" +
                "WHERE f.USER_ID = ?\n" +
                "  AND u.USER_ID != ?\n" +
                "  AND f.STATUS = 'Accepted';";
    }

    public static String getRequests() {
        return "SELECT u.USERNAME, f.STATUS\n" +
                "FROM user u\n" +
                "JOIN friends f ON (u.USER_ID = f.FRIEND_ID OR u.USER_ID = f.USER_ID)\n" +
                "WHERE f.USER_ID = ?\n" +
                "  AND u.USER_ID != ?\n" +
                "  AND (f.STATUS = 'Pending' OR f.STATUS = 'Sent');";
    }
}
