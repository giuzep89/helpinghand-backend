package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.ChatOutputDTO;
import com.giuzep89.helpinghandbackend.dtos.MessageInputDTO;
import com.giuzep89.helpinghandbackend.dtos.MessageOutputDTO;
import com.giuzep89.helpinghandbackend.services.ChatService;
import com.giuzep89.helpinghandbackend.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<List<ChatOutputDTO>> getUserChats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getUserChats(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ChatOutputDTO> createChat(
            @RequestBody Long recipientId,
            @AuthenticationPrincipal UserDetails userDetails) {
        ChatOutputDTO created = chatService.createChat(recipientId, userDetails.getUsername());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatOutputDTO> getChat(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getChat(id, userDetails.getUsername()));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageOutputDTO>> getMessagesForChat(
            @PathVariable Long chatId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(messageService.getMessagesForChat(chatId, userDetails.getUsername()));
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageOutputDTO> sendMessage(
            @PathVariable Long chatId,
            @Valid @RequestBody MessageInputDTO messageInputDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        messageInputDTO.setChatId(chatId);
        MessageOutputDTO created = messageService.sendMessage(messageInputDTO, userDetails.getUsername());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}