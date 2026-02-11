package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.UserQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * UserService class provides services related to the User.
 */
public class UserService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }


    public int login(String username, String password) {
        final String query = UserQuery.getUserByUsernameAndPassword();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DbManager.setAccountID(resultSet.getInt("USER_ID"));
                    return resultSet.getInt("USER_ID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int register(String username, String password) {
        final String query = UserQuery.insertUser();

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, password);

            int affected = statement.executeUpdate();
            if (affected == 0) {
                return -1;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    DbManager.setAccountID(keys.getInt(1));
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public String checkIfUserExists(String username) {
        final String query = UserQuery.getUserByUsername();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("USERNAME");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void editUser(int userId, String username, String password) {
        final String query = UserQuery.editUser();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3, userId);

            int updatedRows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}