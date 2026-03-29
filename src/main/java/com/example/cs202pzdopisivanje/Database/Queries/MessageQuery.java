package com.example.cs202pzdopisivanje.Database.Queries;

/**
 * MessageQuery holds all the SQL Queries as strings related to messages.
 */
public class MessageQuery {
    
    /**
     * SQL query to insert a new message into the database.
     */
    public static String insertMessage(){
        return "INSERT INTO message (user_id, chat_id, message_string, time_sent) VALUES (?, ?, ?, NOW())";
    }

    /**
     * SQL query to retrieve messages by the id of the chat.
     */
    public static String getMessagesById() {
        return "SELECT \n" +
                "m.MESSAGE_ID, \n" +
                "u.USERNAME, \n" +
                "m.CHAT_ID, \n" +
                "m.MESSAGE_STRING, \n" +
                "m.TIME_SENT \n" +
                "FROM message m\n" +
                "JOIN user u ON m.USER_ID = u.USER_ID\n" +
                "WHERE m.CHAT_ID = ?\n" +
                "ORDER BY m.TIME_SENT ASC;";
    }
}