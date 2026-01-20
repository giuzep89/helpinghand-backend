package com.giuzep89.helpinghandbackend.dtos;

public class MessageInputDTO {
    private Long chatId;
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
