package com.giuzep89.helpinghandbackend.dtos;

import java.time.LocalDateTime;

public class MessageOutputDTO {
    private Long id;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
    private boolean isMe; // to quickly distinguish messages, useful in frontend

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
}
