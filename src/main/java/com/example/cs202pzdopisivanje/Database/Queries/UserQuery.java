package com.example.cs202pzdopisivanje.Database.Queries;

public class UserQuery {
    public static String getUserByAccountId() {return "SELECT user.username, user.password FROM user WHERE account.Id = ?"; }
    public static String getUserByUsername() {return "SELECT user.username FROM user WHERE user.username = ?";};
    public static String insertUser() {return "INSERT INTO user (username, password) VALUES (?, ?)";};
    public static String getUserByUsernameAndPassword() {return "SELECT user.USER_ID FROM user WHERE user.username = ? AND user.password = ?";};
    public static String editUser() {return "UPDATE user SET username = ?, password = ? WHERE user.USER_ID = ?";};
}
