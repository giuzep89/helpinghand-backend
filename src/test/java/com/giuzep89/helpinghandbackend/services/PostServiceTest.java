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
    public void setUp() {
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
    public void shouldThrowRecordNotFound_WhenGetAllPostsUserNotFound() {
        //arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.getAllPosts("unknown", 0, 20));
    }

    @Test
    public void shouldReturnPagedPosts_WhenGetAllPosts() {
        //arrange
        testUser.setFriends(new ArrayList<>(List.of(friendUser)));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        List<Post> posts = List.of(testHelpRequest, testActivity);
        Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(0, 20), 2);
        when(postRepository.findByAuthorInOrderByCreatedAtDesc(List.of(friendUser, testUser), PageRequest.of(0, 20)))
                .thenReturn(postPage);

        //act
        Page<PostOutputDTO> result = postService.getAllPosts("testuser", 0, 20);

        //assert
        assertEquals(2, result.getTotalElements());
    }

    // createHelpRequest ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenCreateHelpRequestUserNotFound() {
        //arrange
        HelpRequestInputDTO inputDTO = new HelpRequestInputDTO();
        inputDTO.setDescription("Need help");
        inputDTO.setHelpType(HelpType.GARDENING);
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.createHelpRequest(inputDTO, "unknown"));
    }

    @Test
    public void shouldSaveAndReturnDto_WhenCreateHelpRequest() {
        //arrange
        HelpRequestInputDTO inputDTO = new HelpRequestInputDTO();
        inputDTO.setDescription("Need help with gardening");
        inputDTO.setLocation("Amsterdam");
        inputDTO.setHelpType(HelpType.GARDENING);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(HelpRequest.class))).thenReturn(testHelpRequest);

        //act
        PostOutputDTO result = postService.createHelpRequest(inputDTO, "testuser");

        //assert
        assertNotNull(result);
        assertEquals("HELP_REQUEST", result.getPostType());
        assertEquals(HelpType.GARDENING, result.getHelpType());
    }

    // createActivity ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenCreateActivityUserNotFound() {
        //arrange
        ActivityInputDTO inputDTO = new ActivityInputDTO();
        inputDTO.setDescription("Sports event");
        inputDTO.setActivityType(ActivityType.SPORTS);
        inputDTO.setEventDate(LocalDateTime.now().plusDays(7));
        inputDTO.setLocation("Park");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.createActivity(inputDTO, "unknown"));
    }

    @Test
    public void shouldSaveAndReturnDto_WhenCreateActivity() {
        //arrange
        ActivityInputDTO inputDTO = new ActivityInputDTO();
        inputDTO.setDescription("Sports event");
        inputDTO.setLocation("Park");
        inputDTO.setActivityType(ActivityType.SPORTS);
        inputDTO.setEventDate(LocalDateTime.now().plusDays(7));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Activity.class))).thenReturn(testActivity);

        //act
        PostOutputDTO result = postService.createActivity(inputDTO, "testuser");

        //assert
        assertNotNull(result);
        assertEquals("ACTIVITY", result.getPostType());
        assertEquals(ActivityType.SPORTS, result.getActivityType());
    }

    // deletePost ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenDeletePostNotFound() {
        //arrange
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.deletePost(99L, "testuser"));
    }

    @Test
    public void shouldThrowUnauthorized_WhenDeletePostByNonAuthor() {
        //arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        //act & assert
        assertThrows(UnauthorizedException.class,
                () -> postService.deletePost(1L, "otheruser"));
    }

    @Test
    public void shouldDeletePost_WhenAuthor() {
        //arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        //act
        postService.deletePost(1L, "testuser");

        //assert
        verify(postRepository).delete(testHelpRequest);
    }

    // markHelpFound ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenMarkHelpFoundPostNotFound() {
        //arrange
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.markHelpFound(99L, List.of(1L), "testuser"));
    }

    @Test
    public void shouldThrowIllegalArgument_WhenMarkHelpFoundOnActivity() {
        //arrange
        when(postRepository.findById(2L)).thenReturn(Optional.of(testActivity));

        //act & assert
        assertThrows(IllegalArgumentException.class,
                () -> postService.markHelpFound(2L, List.of(1L), "testuser"));
    }

    @Test
    public void shouldThrowUnauthorized_WhenMarkHelpFoundByNonAuthor() {
        //arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        //act & assert
        assertThrows(UnauthorizedException.class,
                () -> postService.markHelpFound(1L, List.of(1L), "otheruser"));
    }

    @Test
    public void shouldThrowIllegalArgument_WhenRecipientNotFriend() {
        //arrange
        testUser.setFriends(new ArrayList<>());
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        //act & assert
        assertThrows(IllegalArgumentException.class,
                () -> postService.markHelpFound(1L, List.of(50L), "testuser"));
    }

    @Test
    public void shouldThrowRecordNotFound_WhenRecipientNotFound() {
        //arrange
        testUser.setFriends(new ArrayList<>(List.of(friendUser)));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.markHelpFound(1L, List.of(10L), "testuser"));
    }

    @Test
    public void shouldAwardPrizesAndMarkFound_WhenMarkHelpFound() {
        //arrange
        testUser.setFriends(new ArrayList<>(List.of(friendUser)));
        friendUser.setPrizes(new ArrayList<>());

        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));
        when(userRepository.findById(10L)).thenReturn(Optional.of(friendUser));
        when(userRepository.save(friendUser)).thenReturn(friendUser);
        when(postRepository.save(testHelpRequest)).thenReturn(testHelpRequest);

        //act
        PostOutputDTO result = postService.markHelpFound(1L, List.of(10L), "testuser");

        //assert
        assertNotNull(result);
        assertTrue(testHelpRequest.isHelpFound());
        assertTrue(friendUser.getPrizes().contains(Prize.GARDENING));
    }

    // joinActivity ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenJoinActivityPostNotFound() {
        //arrange
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.joinActivity(99L, "testuser"));
    }

    @Test
    public void shouldThrowIllegalArgument_WhenJoinNonActivity() {
        //arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        //act & assert
        assertThrows(IllegalArgumentException.class,
                () -> postService.joinActivity(1L, "testuser"));
    }

    @Test
    public void shouldThrowRecordNotFound_WhenJoinActivityUserNotFound() {
        //arrange
        when(postRepository.findById(2L)).thenReturn(Optional.of(testActivity));
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.joinActivity(2L, "unknown"));
    }

    @Test
    public void shouldAddAttendee_WhenJoinActivity() {
        //arrange
        testActivity.setAttendees(new ArrayList<>());
        friendUser.setAttendedActivities(new ArrayList<>());

        when(postRepository.findById(2L)).thenReturn(Optional.of(testActivity));
        when(userRepository.findByUsername("frienduser")).thenReturn(Optional.of(friendUser));
        when(userRepository.save(friendUser)).thenReturn(friendUser);
        when(postRepository.save(testActivity)).thenReturn(testActivity);

        //act
        PostOutputDTO result = postService.joinActivity(2L, "frienduser");

        //assert
        assertNotNull(result);
        assertTrue(testActivity.getAttendees().contains(friendUser));
        assertTrue(friendUser.getAttendedActivities().contains(testActivity));
    }

    // deletePostAsAdmin ------------------

    @Test
    public void shouldThrowRecordNotFound_WhenDeletePostAsAdminNotFound() {
        //arrange
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(RecordNotFoundException.class,
                () -> postService.deletePostAsAdmin(99L));
    }

    @Test
    public void shouldDeletePost_WhenAdmin() {
        //arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        //act
        postService.deletePostAsAdmin(1L);

        //assert
        verify(postRepository).delete(testHelpRequest);
    }
}
