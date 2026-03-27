package com.example.cs202pzdopisivanje.Database.Queries;

public class MessageQuery {
    
    /**
     * SQL query to insert a new message into the database
     */
    public static final String INSERT_MESSAGE = 
            "INSERT INTO messages (sender_id, chat_id, message_text, time_sent) VALUES (?, ?, ?, ?)";
    
    /**
     * SQL query to retrieve all messages for a specific chat
     */
    public static final String GET_MESSAGES_BY_CHAT_ID = 
            "SELECT id, sender_id, chat_id, message_text, time_sent FROM messages WHERE chat_id = ? ORDER BY time_sent ASC";
    
    /**
     * SQL query to retrieve a specific message by its ID
     */
    public static final String GET_MESSAGE_BY_ID = 
            "SELECT id, sender_id, chat_id, message_text, time_sent FROM messages WHERE id = ?";
    
    /**
     * SQL query to get the latest message from a chat
     */
    public static final String GET_LATEST_MESSAGE_BY_CHAT_ID = 
            "SELECT id, sender_id, chat_id, message_text, time_sent FROM messages WHERE chat_id = ? ORDER BY time_sent DESC LIMIT 1";
}
