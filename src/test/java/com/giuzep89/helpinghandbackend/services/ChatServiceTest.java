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
    public void setUp() {
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
    public void shouldThrowRecordNotFound_WhenCreateChatCurrentUserNotFound() {
        //arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> chatService.createChat(2L, "unknown"));
    }

    @Test
    public void shouldThrowRecordNotFound_WhenCreateChatRecipientNotFound() {
        //arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> chatService.createChat(99L, "testuser"));
    }

    @Test
    public void shouldReturnExistingChat_WhenChatAlreadyExistsAsUserOne() {
        //arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRepository.findByUserOneAndUserTwo(testUser, otherUser)).thenReturn(Optional.of(testChat));

        //act
        ChatOutputDTO result = chatService.createChat(2L, "testuser");

        //assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("otheruser", result.getOtherUserUsername());
        verify(chatRepository, never()).save(any());
    }

    @Test
    public void shouldReturnExistingChat_WhenChatAlreadyExistsAsUserTwo() {
        //arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRepository.findByUserOneAndUserTwo(testUser, otherUser)).thenReturn(Optional.empty());
        when(chatRepository.findByUserOneAndUserTwo(otherUser, testUser)).thenReturn(Optional.of(testChat));

        //act
        ChatOutputDTO result = chatService.createChat(2L, "testuser");

        //assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(chatRepository, never()).save(any());
    }

    @Test
    public void shouldSaveAndReturnDto_WhenCreateNewChat() {
        //arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRepository.findByUserOneAndUserTwo(testUser, otherUser)).thenReturn(Optional.empty());
        when(chatRepository.findByUserOneAndUserTwo(otherUser, testUser)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(testChat);

        //act
        ChatOutputDTO result = chatService.createChat(2L, "testuser");

        //assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("otheruser", result.getOtherUserUsername());
    }

    // getUserChats ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenGetUserChatsUserNotFound() {
        //arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> chatService.getUserChats("unknown"));
    }

    @Test
    public void shouldReturnEmptyList_WhenUserHasNoChats() {
        //arrange
        testUser.setInitiatedChats(new ArrayList<>());
        testUser.setReceivedChats(new ArrayList<>());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        //act
        List<ChatOutputDTO> result = chatService.getUserChats("testuser");

        //assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnAllChats_WhenUserHasInitiatedAndReceivedChats() {
        //arrange
        Chat receivedChat = new Chat(otherUser, testUser);
        ReflectionTestUtils.setField(receivedChat, "id", 20L);

        testUser.setInitiatedChats(new ArrayList<>(List.of(testChat)));
        testUser.setReceivedChats(new ArrayList<>(List.of(receivedChat)));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        //act
        List<ChatOutputDTO> result = chatService.getUserChats("testuser");

        //assert
        assertEquals(2, result.size());
    }

    // getChat ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenGetChatUserNotFound() {
        //arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> chatService.getChat(10L, "unknown"));
    }

    @Test
    public void shouldThrowRecordNotFound_WhenGetChatNotFound() {
        //arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatRepository.findById(99L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> chatService.getChat(99L, "testuser"));
    }

    @Test
    public void shouldThrowUnauthorized_WhenGetChatUserNotParticipant() {
        //arrange
        User thirdUser = new User();
        thirdUser.setUsername("thirduser");
        ReflectionTestUtils.setField(thirdUser, "id", 99L);

        when(userRepository.findByUsername("thirduser")).thenReturn(Optional.of(thirdUser));
        when(chatRepository.findById(10L)).thenReturn(Optional.of(testChat));

        //act & assert
        assertThrows(UnauthorizedException.class,
                () -> chatService.getChat(10L, "thirduser"));
    }

    @Test
    public void shouldReturnChat_WhenUserIsUserOne() {
        //arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatRepository.findById(10L)).thenReturn(Optional.of(testChat));

        //act
        ChatOutputDTO result = chatService.getChat(10L, "testuser");

        //assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("otheruser", result.getOtherUserUsername());
    }

    @Test
    public void shouldReturnChat_WhenUserIsUserTwo() {
        //arrange
        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));
        when(chatRepository.findById(10L)).thenReturn(Optional.of(testChat));

        //act
        ChatOutputDTO result = chatService.getChat(10L, "otheruser");

        //assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("testuser", result.getOtherUserUsername());
    }
}
