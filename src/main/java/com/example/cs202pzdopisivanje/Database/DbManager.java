package com.example.cs202pzdopisivanje.Database;

import Enums.Constants;
import com.example.cs202pzdopisivanje.Services.ChatService;
import com.example.cs202pzdopisivanje.Services.UserService;

import java.sql.*;

/**
 * DbManager class responsible for managing database operations.
 */
public class DbManager {
    /**
     * Represents the unique account ID.
     */
    private static int accountID = 0;

    /**
     * Gets the current account ID.
     *
     * @return The current account ID.
     */
    public static int getAccountID() {
        return accountID;
    }

    /**
     * Sets the account ID.
     *
     * @param accountID The account ID to be set.
     */
    public static void setAccountID(int accountID) {
        DbManager.accountID = accountID;
    }

    /**
     * Represents the database URL.
     */
    public static final String URL = "jdbc:mysql://localhost/dopisivanje";

    /**
     * Represents the connection status to the database.
     */
    private static boolean connected = false;

    /**
     * Checks if the application is connected to the database.
     *
     * @return True if connected, false otherwise.
     */
    public static boolean isConnected() {
        return connected;
    }

    /**
     * Represents the database connection.
     */
    public static Connection connection;

    public static void connect() {
        if (isConnected()) return;
        try {
            connection = DriverManager.getConnection(URL, "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connected = true;

        System.out.println("connected to db");
    }

    /**
     * Closes the connection to the database.
     */
    public static void disconnect() {
        if (!isConnected()) return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connected = false;
    }

    private static UserService userService = null;
    public static UserService userService() throws Exception {
        if (!connected) {
            throw new Exception(Constants.notConnected);
        }

        if (userService == null) {
            userService = new UserService(connection);
        }

        return userService;
    }

    private static ChatService chatService = null;
    public static ChatService ChatService() throws Exception {
        if (!connected) {
            throw new Exception(Constants.notConnected);
        }

        if (chatService == null) {
            chatService = new ChatService(connection);
        }

        return chatService;
    }
}