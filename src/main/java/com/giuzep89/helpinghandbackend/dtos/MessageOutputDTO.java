package com.giuzep89.helpinghandbackend.dtos;

import java.time.LocalDateTime;

public class MessageOutputDTO {
    private Long id;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
    private boolean isMe; // to quickly distinguish messages, useful in frontend

    public void setId(Long id) {
    }

    public void setSenderUsername(String username) {
    }

    public void setContent(String content) {
    }

    public void setTimestamp(LocalDateTime timestamp) {
    }

    public void setMe(boolean equals) {
    }
}
