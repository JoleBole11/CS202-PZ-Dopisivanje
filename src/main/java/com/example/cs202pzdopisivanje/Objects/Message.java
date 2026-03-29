package com.example.cs202pzdopisivanje.Objects;

import java.io.Serializable;

/**
 * Represents the Message object.
 */
public class Message implements Serializable {
    /**
     * The ID of the message.
     */
    private int id;

    /**
     * The username of the sender.
     */
    private String username;

    /**
     * The ID of the chat the message belongs to.
     */
    private int chatId;

    /**
     * The text of the message.
     */
    private String message;

    public Message(int id, String username, int chatId, String message) {
        this.id = id;
        this.username = username;
        this.chatId = chatId;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}