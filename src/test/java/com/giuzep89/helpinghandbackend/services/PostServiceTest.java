package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.ActivityInputDTO;
import com.giuzep89.helpinghandbackend.dtos.HelpRequestInputDTO;
import com.giuzep89.helpinghandbackend.dtos.PostOutputDTO;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.exceptions.UnauthorizedException;
import com.giuzep89.helpinghandbackend.models.*;
import com.giuzep89.helpinghandbackend.repositories.PostRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostService postService;

    private User testUser;
    private User friendUser;
    private HelpRequest testHelpRequest;
    private Activity testActivity;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");

        friendUser = new User();
        friendUser.setUsername("frienduser");
        friendUser.setEmail("friend@test.com");
        friendUser.setPassword("password123");
        ReflectionTestUtils.setField(friendUser, "id", 10L);

        testHelpRequest = new HelpRequest();
        testHelpRequest.setId(1L);
        testHelpRequest.setDescription("Need help with gardening");
        testHelpRequest.setHelpType(HelpType.GARDENING);
        testHelpRequest.setAuthor(testUser);
        testHelpRequest.setCreatedAt(LocalDateTime.now());

        testActivity = new Activity();
        testActivity.setId(2L);
        testActivity.setDescription("Sports event");
        testActivity.setActivityType(ActivityType.SPORTS);
        testActivity.setAuthor(testUser);
        testActivity.setCreatedAt(LocalDateTime.now());
        testActivity.setEventDate(LocalDateTime.now().plusDays(7));
    }

    // getAllPosts ------------------

    @Test
    void shouldThrowRecordNotFound_WhenGetAllPostsUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.getAllPosts("unknown", 0, 20));
    }

    @Test
    void shouldReturnPagedPosts_WhenGetAllPosts() {
        testUser.setFriends(new ArrayList<>(List.of(friendUser)));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        List<Post> posts = List.of(testHelpRequest, testActivity);
        Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(0, 20), 2);
        when(postRepository.findByAuthorInOrderByCreatedAtDesc(List.of(friendUser, testUser), PageRequest.of(0, 20)))
                .thenReturn(postPage);

        Page<PostOutputDTO> result = postService.getAllPosts("testuser", 0, 20);

        assertEquals(2, result.getTotalElements());
    }

    // createHelpRequest ------------------

    @Test
    void shouldThrowRecordNotFound_WhenCreateHelpRequestUserNotFound() {
        HelpRequestInputDTO inputDTO = new HelpRequestInputDTO();
        inputDTO.setDescription("Need help");
        inputDTO.setHelpType(HelpType.GARDENING);
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.createHelpRequest(inputDTO, "unknown"));
    }

    @Test
    void shouldSaveAndReturnDto_WhenCreateHelpRequest() {
        HelpRequestInputDTO inputDTO = new HelpRequestInputDTO();
        inputDTO.setDescription("Need help with gardening");
        inputDTO.setLocation("Amsterdam");
        inputDTO.setHelpType(HelpType.GARDENING);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(HelpRequest.class))).thenReturn(testHelpRequest);

        PostOutputDTO result = postService.createHelpRequest(inputDTO, "testuser");

        assertNotNull(result);
        assertEquals("HELP_REQUEST", result.getPostType());
        assertEquals(HelpType.GARDENING, result.getHelpType());
    }

    // createActivity ------------------

    @Test
    void shouldThrowRecordNotFound_WhenCreateActivityUserNotFound() {
        ActivityInputDTO inputDTO = new ActivityInputDTO();
        inputDTO.setDescription("Sports event");
        inputDTO.setActivityType(ActivityType.SPORTS);
        inputDTO.setEventDate(LocalDateTime.now().plusDays(7));
        inputDTO.setLocation("Park");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.createActivity(inputDTO, "unknown"));
    }

    @Test
    void shouldSaveAndReturnDto_WhenCreateActivity() {
        ActivityInputDTO inputDTO = new ActivityInputDTO();
        inputDTO.setDescription("Sports event");
        inputDTO.setLocation("Park");
        inputDTO.setActivityType(ActivityType.SPORTS);
        inputDTO.setEventDate(LocalDateTime.now().plusDays(7));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Activity.class))).thenReturn(testActivity);

        PostOutputDTO result = postService.createActivity(inputDTO, "testuser");

        assertNotNull(result);
        assertEquals("ACTIVITY", result.getPostType());
        assertEquals(ActivityType.SPORTS, result.getActivityType());
    }

    // deletePost ------------------

    @Test
    void shouldThrowRecordNotFound_WhenDeletePostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.deletePost(99L, "testuser"));
    }

    @Test
    void shouldThrowUnauthorized_WhenDeletePostByNonAuthor() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        assertThrows(UnauthorizedException.class,
                () -> postService.deletePost(1L, "otheruser"));
    }

    @Test
    void shouldDeletePost_WhenAuthor() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        postService.deletePost(1L, "testuser");

        verify(postRepository).delete(testHelpRequest);
    }

    // markHelpFound ------------------

    @Test
    void shouldThrowRecordNotFound_WhenMarkHelpFoundPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.markHelpFound(99L, List.of(1L), "testuser"));
    }

    @Test
    void shouldThrowIllegalArgument_WhenMarkHelpFoundOnActivity() {
        when(postRepository.findById(2L)).thenReturn(Optional.of(testActivity));

        assertThrows(IllegalArgumentException.class,
                () -> postService.markHelpFound(2L, List.of(1L), "testuser"));
    }

    @Test
    void shouldThrowUnauthorized_WhenMarkHelpFoundByNonAuthor() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        assertThrows(UnauthorizedException.class,
                () -> postService.markHelpFound(1L, List.of(1L), "otheruser"));
    }

    @Test
    void shouldThrowIllegalArgument_WhenRecipientNotFriend() {
        testUser.setFriends(new ArrayList<>());
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        assertThrows(IllegalArgumentException.class,
                () -> postService.markHelpFound(1L, List.of(50L), "testuser"));
    }

    @Test
    void shouldThrowRecordNotFound_WhenRecipientNotFound() {
        testUser.setFriends(new ArrayList<>(List.of(friendUser)));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.markHelpFound(1L, List.of(10L), "testuser"));
    }

    @Test
    void shouldAwardPrizesAndMarkFound_WhenMarkHelpFound() {
        testUser.setFriends(new ArrayList<>(List.of(friendUser)));
        friendUser.setPrizes(new ArrayList<>());

        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));
        when(userRepository.findById(10L)).thenReturn(Optional.of(friendUser));
        when(userRepository.save(friendUser)).thenReturn(friendUser);
        when(postRepository.save(testHelpRequest)).thenReturn(testHelpRequest);

        PostOutputDTO result = postService.markHelpFound(1L, List.of(10L), "testuser");

        assertNotNull(result);
        assertTrue(testHelpRequest.isHelpFound());
        assertTrue(friendUser.getPrizes().contains(Prize.GARDENING));
    }

    // joinActivity ------------------

    @Test
    void shouldThrowRecordNotFound_WhenJoinActivityPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.joinActivity(99L, "testuser"));
    }

    @Test
    void shouldThrowIllegalArgument_WhenJoinNonActivity() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        assertThrows(IllegalArgumentException.class,
                () -> postService.joinActivity(1L, "testuser"));
    }

    @Test
    void shouldThrowRecordNotFound_WhenJoinActivityUserNotFound() {
        when(postRepository.findById(2L)).thenReturn(Optional.of(testActivity));
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.joinActivity(2L, "unknown"));
    }

    @Test
    void shouldAddAttendee_WhenJoinActivity() {
        testActivity.setAttendees(new ArrayList<>());
        friendUser.setAttendedActivities(new ArrayList<>());

        when(postRepository.findById(2L)).thenReturn(Optional.of(testActivity));
        when(userRepository.findByUsername("frienduser")).thenReturn(Optional.of(friendUser));
        when(userRepository.save(friendUser)).thenReturn(friendUser);
        when(postRepository.save(testActivity)).thenReturn(testActivity);

        PostOutputDTO result = postService.joinActivity(2L, "frienduser");

        assertNotNull(result);
        assertTrue(testActivity.getAttendees().contains(friendUser));
        assertTrue(friendUser.getAttendedActivities().contains(testActivity));
    }

    // deletePostAsAdmin ------------------

    @Test
    void shouldThrowRecordNotFound_WhenDeletePostAsAdminNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> postService.deletePostAsAdmin(99L));
    }

    @Test
    void shouldDeletePost_WhenAdmin() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        postService.deletePostAsAdmin(1L);

        verify(postRepository).delete(testHelpRequest);
    }
}
