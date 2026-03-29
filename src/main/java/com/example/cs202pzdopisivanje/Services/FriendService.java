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
                        String currentUsername = getCurrentUsername();
                        String chatName1 = currentUsername + friendName;
                        String chatName2 = friendName + currentUsername;
                        
                        // Find the chat ID for this friendship
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
        // First check if the user exists and get their ID
        int friendUserId = getUserIdByUsername(username);
        if (friendUserId == -1) {
            return "User not found";
        }
        
        // Prevent sending friend request to yourself
        if (friendUserId == DbManager.getAccountID()) {
            return "Cannot send friend request to yourself";
        }
        
        // Check if any relationship already exists between these users
        if (anyRelationshipExists(DbManager.getAccountID(), friendUserId)) {
            return "Exists";
        }
        
        final String query = FriendQuery.sendFriendRequest();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Use user IDs instead of usernames to avoid conflicts
            statement.setInt(1, DbManager.getAccountID());
            statement.setInt(2, friendUserId);
            statement.setInt(3, friendUserId);
            statement.setInt(4, DbManager.getAccountID());
        
            int affected = statement.executeUpdate();

            if (affected >= 1) {
                return "Success";
            } else {
                return "Failed to send friend request";
            }
        
        } catch (SQLException e) {
            // Check if it's a duplicate entry error
            if (e.getMessage().contains("Duplicate entry")) {
                return "Friend request already exists";
            }
        
            System.err.println("SQL Error in sendFriendRequest: " + e.getMessage());
            e.printStackTrace();
            return "Database error occurred";
        }
    }

    private int getUserIdByUsername(String username) {
        final String query = "SELECT user_id FROM user WHERE USERNAME = ?";
        
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

    private boolean anyRelationshipExists(int userId1, int userId2) {
        final String checkQuery = "SELECT COUNT(*) FROM friends WHERE " +
                                  "(USER_ID = ? AND FRIEND_ID = ?) OR (USER_ID = ? AND FRIEND_ID = ?)";

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
            return true; // Return true to prevent insertion in case of error
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
        
            if (affected >= 1) {
                // Create a private chat between the two friends
                createPrivateChat(username);
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

    private void createPrivateChat(String friendUsername) {
        try {
            // Get the current user's username
            String currentUsername = getCurrentUsername();
            
            // Create chat name combining both usernames
            String chatName = currentUsername + friendUsername;
            
            // Use GroupService to create a private chat (is_group = 0)
            GroupService groupService = new GroupService(connection);
            int chatId = groupService.insertGroup(chatName, 0); // 0 for is_group = false
            
            if (chatId != -1) {
                // Add both users to the chat
                addUsersToChat(chatId, DbManager.getAccountID(), friendUsername);
                System.out.println("Private chat created successfully with ID: " + chatId);
            } else {
                System.out.println("Failed to create private chat");
            }
            
        } catch (Exception e) {
            System.out.println("Error creating private chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getCurrentUsername() {
        final String query = "SELECT USERNAME FROM user WHERE USER_ID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("USERNAME");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting current username: " + e.getMessage());
        }
        
        return "Unknown";
    }

    private void addUsersToChat(int chatId, int currentUserId, String friendUsername) {
        // Get friend's user ID
        int friendUserId = getUserIdByUsername(friendUsername);
        
        if (friendUserId == -1) {
            System.err.println("Could not find friend's user ID");
            return;
        }
        
        // Add both users to the chat (assuming there's a chat_members table)
        final String insertMemberQuery = "INSERT INTO chat_members (CHAT_ID, USER_ID) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(insertMemberQuery)) {
            // Add current user
            statement.setInt(1, chatId);
            statement.setInt(2, currentUserId);
            statement.executeUpdate();
            
            // Add friend
            statement.setInt(1, chatId);
            statement.setInt(2, friendUserId);
            statement.executeUpdate();
        
    } catch (SQLException e) {
        System.err.println("Error adding users to chat: " + e.getMessage());
    }
}

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
            System.err.println("SQL Error in denyFriendRequest: " + e.getMessage());
            e.printStackTrace();
            return "Fail";
        }
    }

    public String removeFriend(String username) {
        final String query = FriendQuery.removeFriend();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, DbManager.getAccountID());
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setInt(4, DbManager.getAccountID());

            int affected = statement.executeUpdate();
            
            if (affected >= 1) {
                // Delete the private chat between the two friends
                deletePrivateChat(username);
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

    private void deletePrivateChat(String friendUsername) {
        try {
            // Get the current user's username
            String currentUsername = getCurrentUsername();

            // Create both possible chat name combinations
            String chatName1 = currentUsername + friendUsername;
            String chatName2 = friendUsername + currentUsername;

            // Find and delete the chat by searching for both name patterns
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
            System.err.println("Error deleting private chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int findChatIdByName(String chatName1) {
        // Assuming the chat table is named 'chat' based on your GroupService
        final String query = "SELECT CHAT_ID FROM chat WHERE CHAT_NAME = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chatName1);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CHAT_ID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding chat by name: " + e.getMessage());
        }

        return -1;
    }

    private void deleteChatById(int chatId) {
        // Assuming the chat table is named 'chat' based on your GroupService
        final String query = "DELETE FROM chat WHERE CHAT_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatId);
            int deleted = statement.executeUpdate();
            System.out.println("Deleted " + deleted + " chat with ID: " + chatId);
        } catch (SQLException e) {
            System.err.println("Error deleting chat: " + e.getMessage());
        }
    }
}