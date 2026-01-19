package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.MessageInputDTO;
import com.giuzep89.helpinghandbackend.dtos.MessageOutputDTO;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.exceptions.UnauthorizedException;
import com.giuzep89.helpinghandbackend.mappers.MessageMapper;
import com.giuzep89.helpinghandbackend.models.Chat;
import com.giuzep89.helpinghandbackend.models.Message;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.ChatRepository;
import com.giuzep89.helpinghandbackend.repositories.MessageRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public MessageOutputDTO sendMessage(MessageInputDTO dto, String senderUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow(() -> new RecordNotFoundException("Chat not found"));

        if (!isParticipant(chat, sender)) {
            throw new UnauthorizedException("User is not part of this chat");
        }

        Message message = new Message(chat, sender, dto.getContent());
        message = messageRepository.save(message);

        return MessageMapper.toDTO(message, senderUsername);
    }

    public List<MessageOutputDTO> getMessagesForChat(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RecordNotFoundException("Chat not found"));

        if (!isParticipant(chat, user)) {
            throw new UnauthorizedException("User is not part of this chat");
        }

        return chat.getMessages().stream()
                .map(message -> MessageMapper.toDTO(message, username))
                .toList();
    }

    private boolean isParticipant(Chat chat, User user) {
        return chat.getUserOne().getId().equals(user.getId())
                || chat.getUserTwo().getId().equals(user.getId());
    }
}
