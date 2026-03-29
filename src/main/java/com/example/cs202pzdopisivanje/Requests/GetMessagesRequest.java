package com.example.cs202pzdopisivanje.Requests;

import com.example.cs202pzdopisivanje.Objects.Message;

import java.util.List;

/**
 * Represents the communication request for getting messages.
 */
public class GetMessagesRequest extends Request {
    /**
     * The ID of the chat.
     */
    private int chatId;
    /**
     * The List containing messages.
     */
    private List<Message> messages;

    public GetMessagesRequest(int chatId) {
        this.chatId = chatId;
    }

    public int getChatId() {
        return chatId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
