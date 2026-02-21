package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.FriendQuery;
import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}