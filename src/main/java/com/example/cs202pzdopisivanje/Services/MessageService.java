package com.example.cs202pzdopisivanje.Services;

import com.example.cs202pzdopisivanje.Database.Queries.MessageQuery;
import com.example.cs202pzdopisivanje.Objects.Message;

import java.sql.*;
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
     * @return The ID of the Message object, or -1 if failed
     */
    public int sendMessage(int senderId, int chatId, String messageText) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                MessageQuery.insertMessage(),
                Statement.RETURN_GENERATED_KEYS
            );
            
            statement.setInt(1, senderId);
            statement.setInt(2, chatId);
            statement.setString(3, messageText);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
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
            PreparedStatement statement = connection.prepareStatement(MessageQuery.getMessagesById());
            statement.setInt(1, chatId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getString("username"),
                        resultSet.getInt("chat_id"),
                        resultSet.getString("message_string"),
                        resultSet.getString("time_sent")
                );
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}