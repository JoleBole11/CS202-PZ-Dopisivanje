package com.example.cs202pzdopisivanje.Services;


import java.sql.Connection;

/**
 * ChatService class provides services related to the Chat.
 */
public class ChatService {

    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    public ChatService(Connection connection) {
        this.connection = connection;
    }


}