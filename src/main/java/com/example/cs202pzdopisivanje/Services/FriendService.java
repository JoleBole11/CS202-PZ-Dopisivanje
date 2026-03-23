package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.FriendQuery;
import com.example.cs202pzdopisivanje.Database.Queries.UserQuery;
import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FriendService class provides services related to Friends.
 */
public class FriendService {

    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    public FriendService(Connection connection) {
        this.connection = connection;
    }

    public List<String> getUserFriends() {
        final String query = FriendQuery.getFriends();
        List<String> friends = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setInt(2, DbManager.getAccountID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        String friendName = resultSet.getString("USERNAME");
                        friends.add(friendName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friends;
    }

    public List<FriendRequestObject> getUserRequests() {
        final String query = FriendQuery.getRequests();
        List<FriendRequestObject> friendRequests = new ArrayList<>();
        FriendRequestObject tempFriendRequest;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setInt(2, DbManager.getAccountID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        String friendName = resultSet.getString("USERNAME");
                        System.out.println(friendName);
                        String friendStatus = resultSet.getString("STATUS");
                        System.out.println(friendStatus);
                        tempFriendRequest = new FriendRequestObject(friendName, friendStatus);
                        friendRequests.add(tempFriendRequest);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friendRequests;
    }

    public String sendFriendRequest(String username){
        if (friendRequestExists(username)) {
            return "Exists";
        }
        
        final String query = FriendQuery.sendFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());
            
            int affected = statement.executeUpdate();

            if (affected == 2) {
                return "Success";
            } else {
                return "User not found or request already exists.";
            }
        
        } catch (SQLException e) {
            System.err.println("SQL Error in sendFriendRequest: " + e.getMessage());
            e.printStackTrace();
            return "Database error occurred";
        }
    }

    private boolean friendRequestExists(String username) {
        final String checkQuery = FriendQuery.checkFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(checkQuery)) {
            int currentUserId = DbManager.getAccountID();
            statement.setInt(1, currentUserId);
            statement.setString(2, username);
            statement.setInt(3, currentUserId);
            statement.setString(4, username);
            statement.setInt(5, currentUserId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Existing friend requests found: " + count);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if friend request exists: " + e.getMessage());
            return false;
        }

        return false;
    }

    public String acceptFriendRequest(String username) {
        final String query = FriendQuery.acceptFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());

            int affected = statement.executeUpdate();
        
            if (affected == 2) {
                return "Success";
            } else {
                return "Fail";
            }
        
        } catch (SQLException e) {
            System.err.println("SQL Error in acceptFriendRequest: " + e.getMessage());
            e.printStackTrace();
            return "Fail";
        }
    }

    public String denyFriendRequest(String username) {
        final String query = FriendQuery.acceptFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());

            int affected = statement.executeUpdate();
        
            if (affected == 2) {
                return "Success";
            } else {
                return "Fail";
            }
        
    } catch (SQLException e) {
        System.err.println("SQL Error in denyFriendRequest: " + e.getMessage());
        e.printStackTrace();
        return "Fail";
    }
}

public String removeFriend(String username) {
    final String query = FriendQuery.denyFriendRequest();

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, DbManager.getAccountID());
        statement.setString(2, username);
        statement.setString(3, username);
        statement.setInt(4, DbManager.getAccountID());

        int affected = statement.executeUpdate();
        
        if (affected == 2) {
            return "Success";
        } else {
            return "Fail";
        }
        
    } catch (SQLException e) {
        System.err.println("SQL Error in removeFriend: " + e.getMessage());
        e.printStackTrace();
        return "Fail";
    }
}
}