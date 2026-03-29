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
     * @return "Success" if a group is created successfully, "Error" otherwise
     */
    public String createGroup(String groupName, int isGroup) {

        if (groupExists(groupName)) {
            return "Exists";
        }

        try {
            connection.setAutoCommit(false);

            int groupId = insertGroup(groupName, isGroup);
            if (groupId == -1) {
                connection.rollback();
                return "Error";
            }

            if (Objects.equals(addUserToGroup(groupName, DbManager.getAccountID(),"creator"), "Error")) {
                connection.rollback();
                return "Error";
            }
            
            connection.commit();
            return "Success";
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Inserts a new group into the database.
     *
     * @param groupName The name of the group
     * @param isGroup If it should be a group or not
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
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Adds a user to a group.
     *
     * @param groupName The Name of the group
     * @param userId The ID of the user
     * @param role The role the user will be when added
     * @return "Success" if successful, "Error" otherwise
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
            e.printStackTrace();
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