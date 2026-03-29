package com.example.cs202pzdopisivanje.Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the Message object.
 */
public class Message implements Serializable {
    private int id;
    private String username;
    private int chatId;
    private String message;
    private String timeSent;

    public Message(int id, String username, int chatId, String message, String timeSent) {
        this.id = id;
        this.username = username;
        this.chatId = chatId;
        this.message = message;
        this.timeSent = timeSent;
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

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}