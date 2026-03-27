package com.example.cs202pzdopisivanje.Objects;

import java.util.Objects;

public class Chat {
    private int chatId;
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
    
    @Override
    public String toString() {
        return chatName; // This will be used if ChatCell is not working properly
    }
}