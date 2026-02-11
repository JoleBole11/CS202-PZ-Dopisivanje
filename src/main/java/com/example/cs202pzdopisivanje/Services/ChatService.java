package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.ChatQuery;


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

    public List<String> getUserGroups() {
        final String query = ChatQuery.getGroups();
        List<String> groups = new ArrayList<>();
        
        System.out.println("DEBUG: Getting groups for user ID: " + DbManager.getAccountID());
        System.out.println("DEBUG: Query: " + query);
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            
            System.out.println("DEBUG: Executing query with parameter: " + DbManager.getAccountID());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean hasResults = false;
                while (resultSet.next()) {
                    hasResults = true;
                    int chatId = resultSet.getInt("CHAT_ID");
                    System.out.println("DEBUG: Found group with CHAT_ID: " + chatId);
                    
                    // Try to get CHAT_NAME first, if it doesn't exist, use CHAT_ID
                    try {
                        String chatName = resultSet.getString("CHAT_NAME");
                        groups.add(chatName != null ? chatName : "Group " + chatId);
                    } catch (SQLException e) {
                        // Column doesn't exist, use CHAT_ID
                        groups.add("Group " + chatId);
                    }
                }
                
                if (!hasResults) {
                    System.out.println("DEBUG: No results found for user ID: " + DbManager.getAccountID());
                }
            }
        } catch (SQLException e) {
            System.out.println("DEBUG: SQL Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        System.out.println("DEBUG: Returning " + groups.size() + " groups: " + groups);
        return groups;
    }
}