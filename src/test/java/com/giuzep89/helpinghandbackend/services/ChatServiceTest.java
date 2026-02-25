package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.ChatOutputDTO;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.exceptions.UnauthorizedException;
import com.giuzep89.helpinghandbackend.models.Chat;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.ChatRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    ChatRepository chatRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ChatService chatService;

    private User testUser;
    private User otherUser;
    private Chat testChat;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");
        ReflectionTestUtils.setField(testUser, "id", 1L);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@test.com");
        otherUser.setPassword("password123");
        ReflectionTestUtils.setField(otherUser, "id", 2L);

        testChat = new Chat(testUser, otherUser);
        ReflectionTestUtils.setField(testChat, "id", 10L);
    }

    // createChat ------------------

    @Test
    void shouldThrowRecordNotFound_WhenCreateChatCurrentUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> chatService.createChat(2L, "unknown"));
    }

    @Test
    void shouldThrowRecordNotFound_WhenCreateChatRecipientNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> chatService.createChat(99L, "testuser"));
    }

    @Test
    void shouldReturnExistingChat_WhenChatAlreadyExistsAsUserOne() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRepository.findByUserOneAndUserTwo(testUser, otherUser)).thenReturn(Optional.of(testChat));

        ChatOutputDTO result = chatService.createChat(2L, "testuser");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("otheruser", result.getOtherUserUsername());
        verify(chatRepository, never()).save(any());
    }

    @Test
    void shouldReturnExistingChat_WhenChatAlreadyExistsAsUserTwo() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRepository.findByUserOneAndUserTwo(testUser, otherUser)).thenReturn(Optional.empty());
        when(chatRepository.findByUserOneAndUserTwo(otherUser, testUser)).thenReturn(Optional.of(testChat));

        ChatOutputDTO result = chatService.createChat(2L, "testuser");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(chatRepository, never()).save(any());
    }

    @Test
    void shouldSaveAndReturnDto_WhenCreateNewChat() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRepository.findByUserOneAndUserTwo(testUser, otherUser)).thenReturn(Optional.empty());
        when(chatRepository.findByUserOneAndUserTwo(otherUser, testUser)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(testChat);

        ChatOutputDTO result = chatService.createChat(2L, "testuser");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("otheruser", result.getOtherUserUsername());
    }

    // getUserChats ------------------

    @Test
    void shouldThrowRecordNotFound_WhenGetUserChatsUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> chatService.getUserChats("unknown"));
    }

    @Test
    void shouldReturnEmptyList_WhenUserHasNoChats() {
        testUser.setInitiatedChats(new ArrayList<>());
        testUser.setReceivedChats(new ArrayList<>());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        List<ChatOutputDTO> result = chatService.getUserChats("testuser");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllChats_WhenUserHasInitiatedAndReceivedChats() {
        Chat receivedChat = new Chat(otherUser, testUser);
        ReflectionTestUtils.setField(receivedChat, "id", 20L);

        testUser.setInitiatedChats(new ArrayList<>(List.of(testChat)));
        testUser.setReceivedChats(new ArrayList<>(List.of(receivedChat)));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        List<ChatOutputDTO> result = chatService.getUserChats("testuser");

        assertEquals(2, result.size());
    }

    // getChat ------------------

    @Test
    void shouldThrowRecordNotFound_WhenGetChatUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> chatService.getChat(10L, "unknown"));
    }

    @Test
    void shouldThrowRecordNotFound_WhenGetChatNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> chatService.getChat(99L, "testuser"));
    }

    @Test
    void shouldThrowUnauthorized_WhenGetChatUserNotParticipant() {
        User thirdUser = new User();
        thirdUser.setUsername("thirduser");
        ReflectionTestUtils.setField(thirdUser, "id", 99L);

        when(userRepository.findByUsername("thirduser")).thenReturn(Optional.of(thirdUser));
        when(chatRepository.findById(10L)).thenReturn(Optional.of(testChat));

        assertThrows(UnauthorizedException.class,
                () -> chatService.getChat(10L, "thirduser"));
    }

    @Test
    void shouldReturnChat_WhenUserIsUserOne() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatRepository.findById(10L)).thenReturn(Optional.of(testChat));

        ChatOutputDTO result = chatService.getChat(10L, "testuser");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("otheruser", result.getOtherUserUsername());
    }

    @Test
    void shouldReturnChat_WhenUserIsUserTwo() {
        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));
        when(chatRepository.findById(10L)).thenReturn(Optional.of(testChat));

        ChatOutputDTO result = chatService.getChat(10L, "otheruser");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("testuser", result.getOtherUserUsername());
    }
}
