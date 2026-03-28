package com.example.cs202pzdopisivanje.Database.Queries;

public class MessageQuery {
    
    /**
     * SQL query to insert a new message into the database
     */
    public static String INSERT_MESSAGE =
            "INSERT INTO message (user_id, chat_id, message_string, time_sent) VALUES (?, ?, ?, NOW())";
    
    /**
     * SQL query to retrieve all messages for a specific chat
     */
    public static String GET_MESSAGES_BY_CHAT_ID =
            "SELECT * FROM (SELECT * FROM message WHERE chat_id = ? ORDER BY time_sent DESC LIMIT 30) sub ORDER BY sub.time_sent ASC";
    
    /**
     * SQL query to retrieve a specific message by its ID
     */
    public static String getMessagesById() {
        return "SELECT \n" +
                "    m.MESSAGE_ID, \n" +
                "    u.USERNAME, \n" +
                "    m.CHAT_ID, \n" +
                "    m.MESSAGE_STRING, \n" +
                "    m.TIME_SENT \n" +
                "FROM message m\n" +
                "JOIN user u ON m.USER_ID = u.USER_ID\n" +
                "WHERE m.CHAT_ID = ?\n" +
                "ORDER BY m.TIME_SENT ASC;";
    }
    
    /**
     * SQL query to get the latest message from a chat
     */
    public static String GET_LATEST_MESSAGE_BY_CHAT_ID =
            "SELECT id, user_id, chat_id, message_string, time_sent FROM messages WHERE chat_id = ? ORDER BY time_sent DESC LIMIT 1";
}