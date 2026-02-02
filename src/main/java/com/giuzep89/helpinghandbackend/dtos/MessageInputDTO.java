package com.giuzep89.helpinghandbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageInputDTO {
    private Long chatId;

    @NotBlank(message = "Message content is required")
    @Size(max = 1000, message = "Message must be 1000 characters or less")
    private String content;

    public Long getChatId() {
        return this.chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
