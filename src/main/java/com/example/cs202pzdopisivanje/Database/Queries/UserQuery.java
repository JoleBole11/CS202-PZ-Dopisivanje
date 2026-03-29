package com.example.cs202pzdopisivanje.Database.Queries;

/**
 * UserQuery holds all the SQL Queries as strings related to users.
 */
public class UserQuery {

    /**
     * SQL query to get a user by their username.
     */
    public static String getUserByUsername() {
        return "SELECT user.username FROM user WHERE user.username = ?";
    }

    /**
     * SQL query to insert a new user into the database.
     */
    public static String insertUser() {
        return "INSERT INTO user (username, password) VALUES (?, ?)";
    }

    /**
     * SQL query to get a user by their username and password.
     */
    public static String getUserByUsernameAndPassword() {
        return "SELECT user.USER_ID FROM user WHERE user.username = ? AND user.password = ?";
    }

    /**
     * SQL query to edit an existing user in the database.
     */
    public static String editUser() {
        return "UPDATE user SET username = ?, password = ? WHERE user.USER_ID = ?";
    };
}
