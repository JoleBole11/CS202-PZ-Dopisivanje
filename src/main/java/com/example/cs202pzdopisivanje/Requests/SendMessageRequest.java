package com.example.cs202pzdopisivanje.Requests;

public class SendMessageRequest extends Request {
    private int senderId;
    private int chatId;
    private String messageText;
    
    public SendMessageRequest(int senderId, int chatId, String messageText) {
        this.senderId = senderId;
        this.chatId = chatId;
        this.messageText = messageText;
    }
    
    public int getSenderId() {
        return senderId;
    }
    
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    public int getChatId() {
        return chatId;
    }
    
    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
