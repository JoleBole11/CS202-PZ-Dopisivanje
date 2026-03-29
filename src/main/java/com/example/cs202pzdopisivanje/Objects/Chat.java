package com.example.cs202pzdopisivanje.Objects;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the Chat object.
 */
public class Chat implements Serializable {
    /**
     * The ID of the chat.
     */
    private int chatId;
    /**
     * The name of the chat.
     */
    private String chatName;

    public Chat(int chatId, String chatName) {
        this.chatId = chatId;
        this.chatName = chatName;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return chatId == chat.chatId && Objects.equals(chatName, chat.chatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, chatName);
    }
}