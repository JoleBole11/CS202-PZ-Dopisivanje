package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.FriendQuery;
import com.example.cs202pzdopisivanje.Objects.Chat;
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

    /**
     * Gets all the current users friends.
     *
     * @return List of Chat objects representing the friends
     */
    public List<Chat> getUserFriends() {
        final String query = FriendQuery.getFriends();
        List<Chat> friends = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setInt(2, DbManager.getAccountID());
            statement.setInt(3, DbManager.getAccountID());
            statement.setInt(4, DbManager.getAccountID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        String friendName = resultSet.getString("USERNAME");
                        String currentUsername = getUsernameById();
                        String chatName1 = currentUsername + friendName;
                        String chatName2 = friendName + currentUsername;

                        int chatId = findChatIdByName(chatName1);
                        if (chatId == -1) {
                            chatId = findChatIdByName(chatName2);
                        }
                        
                        Chat friendChat = new Chat(chatId, friendName);
                        friends.add(friendChat);
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

    /**
     * Gets all the current users' friend requests.
     *
     * @return List of FriendRequest objects representing the requests
     */
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

    /**
     * Sends a friend request to a specific user.
     *
     * @param username The username to whom it will be sent
     * @return "Success" if successful, "Error" or "Exists" otherwise
     */
    public String sendFriendRequest(String username){
        int friendUserId = getUserIdByUsername(username);
        if (friendUserId == -1) {
            return "Error";
        }

        if (friendUserId == DbManager.getAccountID()) {
            return "Error";
        }

        if (anyRelationshipExists(DbManager.getAccountID(), friendUserId)) {
            return "Exists";
        }
        
        final String query = FriendQuery.sendFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setInt(2, friendUserId);
            statement.setInt(3, friendUserId);
            statement.setInt(4, DbManager.getAccountID());
        
            int affected = statement.executeUpdate();

            if (affected >= 1) {
                return "Success";
            } else {
                return "Error";
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    /**
     * Retrieves a user's ID by their username.
     *
     * @param username The username of the user
     * @return The ID of the user
     */
    private int getUserIdByUsername(String username) {
        final String query = FriendQuery.getIDByUsername();
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("USER_ID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user ID by username: " + e.getMessage());
        }
        
        return -1;
    }

    /**
     * Checks if a friend relationship between 2 users exists.
     *
     * @param userId1 The ID of the first user
     * @param userId2 The ID of the second user
     * @return True if exists, false otherwise
     */
    private boolean anyRelationshipExists(int userId1, int userId2) {
        final String checkQuery = FriendQuery.checkRelationship();

        try (PreparedStatement statement = connection.prepareStatement(checkQuery)) {
            statement.setInt(1, userId1);
            statement.setInt(2, userId2);
            statement.setInt(3, userId2);
            statement.setInt(4, userId1);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Existing relationships found: " + count);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if relationship exists: " + e.getMessage());
            return true;
        }

        return false;
    }

    /**
     * Accepts a friend request and creates a chat between them.
     *
     * @param username The username of the user
     * @return "Success" if successful, "Fail" otherwise
     */
    public String acceptFriendRequest(String username) {
        final String query = FriendQuery.acceptFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());

            int affected = statement.executeUpdate();
        
            if (affected >= 1) {
                createPrivateChat(username);
                return "Success";
            } else {
                return "Fail";
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    /**
     * Creates a chat between 2 friends.
     *
     * @param friendUsername The username of the friend
     */
    private void createPrivateChat(String friendUsername) {
        try {
            String currentUsername = getUsernameById();

            String chatName = currentUsername + friendUsername;

            GroupService groupService = new GroupService(connection);
            int chatId = groupService.insertGroup(chatName, 0);
            
            if (chatId != -1) {
                addUsersToChat(chatId, DbManager.getAccountID(), friendUsername);
                System.out.println("Private chat created successfully with ID: " + chatId);
            } else {
                System.out.println("Failed to create private chat");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the username by a user's ID.
     *
     * @return The username of the user
     */
    private String getUsernameById() {
        final String query = FriendQuery.getUsernameByID();
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("USERNAME");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "Error";
    }

    /**
     * Adds friends to a Friend Chat.
     *
     * @param chatId The ID of the chat
     * @param UserId The ID of the current user
     * @param friendUsername The username of the friend
     */
    private void addUsersToChat(int chatId, int UserId, String friendUsername) {
        int friendUserId = getUserIdByUsername(friendUsername);
        
        if (friendUserId == -1) {
            System.err.println("Could not find friend's user ID");
            return;
        }

        final String insertMemberQuery = FriendQuery.insertUserIntoFriends();
        
        try (PreparedStatement statement = connection.prepareStatement(insertMemberQuery)) {
            statement.setInt(1, chatId);
            statement.setInt(2, UserId);
            statement.executeUpdate();

            statement.setInt(1, chatId);
            statement.setInt(2, friendUserId);
            statement.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    /**
     * Denies a friend request and deletes it from the database.
     *
     * @param username The username of the friend
     * @return "Success" if successful, "Fail" otherwise
     */
    public String denyFriendRequest(String username) {
        final String query = FriendQuery.denyFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());

            int affected = statement.executeUpdate();
        
            if (affected >= 1) {
                return "Success";
            } else {
                return "Fail";
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    /**
     * Removes a friend of the user and deletes it from the database.
     *
     * @param username The username of the friend
     * @return "Success" if successful, "Fail" otherwise
     */
    public String removeFriend(String username) {
        final String query = FriendQuery.removeFriend();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());

            int affected = statement.executeUpdate();
            
            if (affected >= 1) {
                deletePrivateChat(username);
                return "Success";
            } else {
                return "Fail";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    /**
     * Deletes Chat from the database.
     */
    private void deletePrivateChat(String friendUsername) {
        try {
            String currentUsername = getUsernameById();

            String chatName1 = currentUsername + friendUsername;
            String chatName2 = friendUsername + currentUsername;

            int chatId = findChatIdByName(chatName1);
            if (chatId == -1) {
                chatId = findChatIdByName(chatName2);
            }

            if (chatId != -1) {
                deleteChatById(chatId);

                System.out.println("Private chat deleted successfully with ID: " + chatId);
            } else {
                System.out.println("Private chat not found for users: " + currentUsername + " and " + friendUsername);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a Chats ID by its name.
     *
     * @param chatName The name of the Chat
     * @return The ID of the user
     */
    private int findChatIdByName(String chatName) {
        final String query = FriendQuery.getChatIDByName();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chatName);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CHAT_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Deletes a Chat by its ID.
     *
     * @param chatId The ID of the chat
     */
    private void deleteChatById(int chatId) {
        final String query = FriendQuery.deleteChatByID();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatId);
            int deleted = statement.executeUpdate();
            System.out.println("Deleted " + deleted + " chat with ID: " + chatId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}