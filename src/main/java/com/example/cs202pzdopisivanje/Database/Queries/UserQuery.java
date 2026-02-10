package com.example.cs202pzdopisivanje.Database.Queries;

public class UserQuery {
    public static String getUserByAccountId() {return "SELECT user.username, user.password FROM user WHERE account.Id = ?"; }
    public static String getUserByUsernameAndPassword() {return "SELECT user.USER_ID FROM user WHERE user.username = ? AND user.password = ?";}
}
