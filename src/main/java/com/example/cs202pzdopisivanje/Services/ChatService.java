package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.ChatQuery;
import com.example.cs202pzdopisivanje.Database.Queries.GroupQuery;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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