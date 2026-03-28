package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.GroupQuery;
import com.example.cs202pzdopisivanje.Objects.Chat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GroupService class provides services related to Groups.
 */
public class GroupService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    public GroupService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new group with the specified name.
     * The current user becomes the creator and is automatically added as a member.
     * 
     * @param groupName The name of the group to create
     * @return "Success" if a group is created successfully, error message otherwise
     */
    public String createGroup(String groupName, int isGroup) {

        if (groupExists(groupName)) {
            return "Exists";
        }

        try {
            connection.setAutoCommit(false); // Start transaction
            
            // Create the group
            int groupId = insertGroup(groupName, isGroup);
            if (groupId == -1) {
                connection.rollback();
                return "Failed to create group";
            }
            
            // Add the creator as a member of the group
            if (Objects.equals(addUserToGroup(groupName, DbManager.getAccountID(),"creator"), "Error")) {
                connection.rollback();
                return "Failed to add creator to group";
            }
            
            connection.commit(); // Commit transaction
            return "Success";
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error occurred";
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Checks if a group with the given name already exists.
     * 
     * @param groupName The name of the group to check
     * @return true if a group exists with the given name, false otherwise
     */
    private boolean groupExists(String groupName) {
        final String query = GroupQuery.checkGroupExists();
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, groupName);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if group exists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Inserts a new group into the database.
     *
     * @param groupName The name of the group
     * @return The ID of the created group, or -1 if failed
     */
    public int insertGroup(String groupName, int isGroup) {
        final String query = GroupQuery.createGroup();
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, groupName);
            statement.setInt(2, isGroup);
            
            int affected = statement.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting group: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Adds a user to a group.
     *
     * @param groupName The Name of the group
     * @return true if successful, false otherwise
     */
    public String addUserToGroup(String groupName, int userId, String role) {
        final String query = GroupQuery.addUserToGroup();
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, groupName);
            statement.setString(3, role);
            
            int affected = statement.executeUpdate();

            if (affected > 0) {
                return "Success";
            }
            return "Error";
        } catch (SQLException e) {
            System.err.println("Error adding user to group: " + e.getMessage());
            return "Error";
        }
    }
    
    /**
     * Gets all groups that the current user is a member of.
     * 
     * @return List of Chat objects representing the groups
     */
    public List<Chat> getUserGroups() {
        final String query = GroupQuery.getGroups();
        List<Chat> groups = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());

            try (ResultSet resultSet = statement.executeQuery()) {
                boolean hasResults = false;
                while (resultSet.next()) {
                    hasResults = true;
                    int chatId = resultSet.getInt("CHAT_ID");
                    String chatName;
                    try {
                        chatName = resultSet.getString("CHAT_NAME");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Chat chat = new Chat(chatId, chatName);
                    groups.add(chat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return groups;
    }
}