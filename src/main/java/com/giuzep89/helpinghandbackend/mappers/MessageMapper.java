package com.giuzep89.helpinghandbackend.mappers;

import com.giuzep89.helpinghandbackend.dtos.MessageOutputDTO;
import com.giuzep89.helpinghandbackend.models.Message;

public class MessageMapper {

    public static MessageOutputDTO toDTO(Message message, String currentUsername) {
        MessageOutputDTO dto = new MessageOutputDTO();
        dto.setId(message.getId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setMe(message.getSender().getUsername().equals(currentUsername));

        return dto;
    }
}
