package com.example.cs202pzdopisivanje.Objects;

public class Message {
    private String message;
    private User sender;
    private String timeSent;

    public Message(String message, User sender, String timeSent) {
        this.message = message;
        this.sender = sender;
        this.timeSent = timeSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
