package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.ChatOutputDTO;
import com.giuzep89.helpinghandbackend.dtos.MessageInputDTO;
import com.giuzep89.helpinghandbackend.dtos.MessageOutputDTO;
import com.giuzep89.helpinghandbackend.services.ChatService;
import com.giuzep89.helpinghandbackend.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatMessagingController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatMessagingController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<ChatOutputDTO>> getUserChats(@RequestParam String username) {
        return ResponseEntity.ok(chatService.getUserChats(username));
    }

    @PostMapping
    public ResponseEntity<ChatOutputDTO> createChat(
            @RequestBody Long recipientId,
            @RequestParam String username) {
        ChatOutputDTO created = chatService.createChat(recipientId, username);
        return ResponseEntity.created(URI.create("/chats/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatOutputDTO> getChat(
            @PathVariable Long id,
            @RequestParam String username) {
        return ResponseEntity.ok(chatService.getChat(id, username));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageOutputDTO>> getMessagesForChat(
            @PathVariable Long chatId,
            @RequestParam String username) {
        return ResponseEntity.ok(messageService.getMessagesForChat(chatId, username));
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageOutputDTO> sendMessage(
            @PathVariable Long chatId,
            @Valid @RequestBody MessageInputDTO messageInputDTO,
            @RequestParam String username) {
        messageInputDTO.setChatId(chatId);
        MessageOutputDTO created = messageService.sendMessage(messageInputDTO, username);
        return ResponseEntity.created(URI.create("/chats/" + chatId + "/messages/" + created.getId())).body(created);
    }
}