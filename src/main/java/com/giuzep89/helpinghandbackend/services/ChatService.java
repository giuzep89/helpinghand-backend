package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.ChatOutputDTO;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.exceptions.UnauthorizedException;
import com.giuzep89.helpinghandbackend.mappers.ChatMapper;
import com.giuzep89.helpinghandbackend.models.Chat;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.ChatRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public ChatOutputDTO createChat(Long recipientId, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RecordNotFoundException("Current user not found"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RecordNotFoundException("Recipient not found"));

        // Check if chat already exists between the two users
        Optional<Chat> existingChat = chatRepository.findChatBetweenUsers(currentUser, recipient);
        if (existingChat.isPresent()) {
            return ChatMapper.toDTO(existingChat.get(), currentUsername);
        }

        Chat chat = new Chat(currentUser, recipient);
        chat = chatRepository.save(chat);

        return ChatMapper.toDTO(chat, currentUsername);
    }

    public List<ChatOutputDTO> getUserChats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        List<Chat> allChats = new ArrayList<>();
        allChats.addAll(user.getInitiatedChats());
        allChats.addAll(user.getReceivedChats());

        return allChats.stream()
                .map(chat -> ChatMapper.toDTO(chat, username))
                .toList();
    }

    public ChatOutputDTO getChat(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RecordNotFoundException("Chat not found"));

        if (!isParticipant(chat, user)) {
            throw new UnauthorizedException("User is not part of this chat");
        }

        return ChatMapper.toDTO(chat, username);
    }

    private boolean isParticipant(Chat chat, User user) {
        return chat.getUserOne().getId().equals(user.getId())
                || chat.getUserTwo().getId().equals(user.getId());
    }
}
