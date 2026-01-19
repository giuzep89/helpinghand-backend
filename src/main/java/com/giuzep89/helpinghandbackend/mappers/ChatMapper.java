package com.giuzep89.helpinghandbackend.mappers;

import com.giuzep89.helpinghandbackend.dtos.ChatOutputDTO;
import com.giuzep89.helpinghandbackend.models.Chat;
import com.giuzep89.helpinghandbackend.models.Message;
import com.giuzep89.helpinghandbackend.models.User;

import java.util.List;

public class ChatMapper {

    public static ChatOutputDTO toDTO(Chat chat, String currentUsername) {
        ChatOutputDTO dto = new ChatOutputDTO();
        dto.setId(chat.getId());

        User otherUser;

        if (chat.getUserOne().getUsername().equals(currentUsername)) {
            otherUser = chat.getUserTwo();
        } else {
            otherUser = chat.getUserOne();
        }

        dto.setOtherUserUsername(otherUser.getUsername());

        // For the last message preview
        List<Message> messages = chat.getMessages();
        if (messages != null && !messages.isEmpty()) {
            Message lastMessage = messages.getLast();
            dto.setLastMessageContent(lastMessage.getContent());
            dto.setLastMessageTime(lastMessage.getTimestamp());
        }

        return dto;
    }

    public static Chat toEntity() {
        // Adding a toEntity here just for convention, but it'll otherwise
        // be filled straight in the Service
        return new Chat();
    }
}