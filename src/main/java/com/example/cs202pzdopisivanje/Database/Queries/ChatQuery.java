package com.example.cs202pzdopisivanje.Database.Queries;

public class ChatQuery {
    public static String getGroups() {return "SELECT c.CHAT_ID, c.CHAT_NAME, c.CREATED_AT\n" +
            "FROM chat c\n" +
            "JOIN chat_member cm ON c.CHAT_ID = cm.CHAT_ID\n" +
            "WHERE cm.USER_ID = ? \n" +
            "  AND c.IS_GROUP = 1;"; }
}
