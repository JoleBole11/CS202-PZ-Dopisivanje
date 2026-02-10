package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.UserQuery;
import com.example.cs202pzdopisivanje.Requests.UserRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserService class provides services related to the User.
 */
public class UserService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    /**
     * Constructs a UserService with the specified database connection.
     *
     * @param connection The database connection to be used for executing SQL queries.
     * @throws RuntimeException If an error occurs during the retrieval of user information.
     */
    public UserService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves user information for a given user and populates the provided UserRequest object.
     *
     * @param request The UserRequest object containing the user ID and empty user information.
     */
    public void getUser(UserRequest request) {
        final String query = UserQuery.getUserByAccountId();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, request.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    request.setUser(resultSet.getString("username"), resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to log in a user by verifying the provided Username and Password against the database.
     *
     * @param username The Username entered by the user for login.
     * @return The account ID if login is successful, -1 otherwise.
     * @throws RuntimeException If an error occurs during the login process.
     */
    public int login(String username, String password) {
        final String query = UserQuery.getUserByUsernameAndPassword();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("USER_ID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}