package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Database.Queries.MessageQuery;
import com.example.cs202pzdopisivanje.Objects.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageService class provides services related to Messages.
 */
public class MessageService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    public MessageService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sends a message to a specific chat.
     * 
     * @param senderId The ID of the user sending the message
     * @param chatId The ID of the chat where the message is being sent
     * @param messageText The content of the message
     * @return The Message object with the generated ID, or null if failed
     */
    public int sendMessage(int senderId, int chatId, String messageText) {
        try {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            PreparedStatement statement = connection.prepareStatement(
                MessageQuery.INSERT_MESSAGE, 
                Statement.RETURN_GENERATED_KEYS
            );
            
            statement.setInt(1, senderId);
            statement.setInt(2, chatId);
            statement.setString(3, messageText);
            statement.setString(4, currentTime);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(3);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }

    /**
     * Retrieves all messages for a specific chat.
     * 
     * @param chatId The ID of the chat
     * @return List of messages in the chat, ordered by time sent
     */
    public List<Message> getMessagesByChatId(int chatId) {
        List<Message> messages = new ArrayList<>();
        
        try {
            PreparedStatement statement = connection.prepareStatement(MessageQuery.GET_MESSAGES_BY_CHAT_ID);
            statement.setInt(1, chatId);
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("id"),
                    resultSet.getInt("sender_id"),
                    resultSet.getInt("chat_id"),
                    resultSet.getString("message_text"),
                    resultSet.getString("time_sent")
                );
                messages.add(message);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }

    /**
     * Retrieves a specific message by its ID.
     * 
     * @param messageId The ID of the message
     * @return The Message object, or null if not found
     */
    public Message getMessageById(int messageId) {
        try {
            PreparedStatement statement = connection.prepareStatement(MessageQuery.GET_MESSAGE_BY_ID);
            statement.setInt(1, messageId);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new Message(
                    resultSet.getInt("id"),
                    resultSet.getInt("sender_id"),
                    resultSet.getInt("chat_id"),
                    resultSet.getString("message_text"),
                    resultSet.getString("time_sent")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving message: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Gets the latest message from a specific chat.
     * 
     * @param chatId The ID of the chat
     * @return The latest Message object, or null if no messages found
     */
    public Message getLatestMessageByChatId(int chatId) {
        try {
            PreparedStatement statement = connection.prepareStatement(MessageQuery.GET_LATEST_MESSAGE_BY_CHAT_ID);
            statement.setInt(1, chatId);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new Message(
                    resultSet.getInt("id"),
                    resultSet.getInt("sender_id"),
                    resultSet.getInt("chat_id"),
                    resultSet.getString("message_text"),
                    resultSet.getString("time_sent")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving latest message: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}