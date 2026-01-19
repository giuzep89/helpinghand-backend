package com.giuzep89.helpinghandbackend.dtos;

public class MessageInputDTO {
    private Long chatId;
    private String content;

    public Long getChatId() {
        return this.chatId;
    }

    public String getContent() {
        return this.content;
    }
}
