package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.ActivityInputDTO;
import com.giuzep89.helpinghandbackend.dtos.HelpRequestInputDTO;
import com.giuzep89.helpinghandbackend.dtos.PostOutputDTO;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.exceptions.UnauthorizedException;
import com.giuzep89.helpinghandbackend.mappers.PostMapper;
import com.giuzep89.helpinghandbackend.models.Activity;
import com.giuzep89.helpinghandbackend.models.HelpRequest;
import com.giuzep89.helpinghandbackend.models.Post;
import com.giuzep89.helpinghandbackend.models.Prize;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.PostRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Page<PostOutputDTO> getAllPosts(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        List<User> allUsersOnFeed = new ArrayList<>(user.getFriends());
        allUsersOnFeed.add(user);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByAuthorInOrderByCreatedAtDesc(allUsersOnFeed, pageable);

        return posts.map(PostMapper::toDTO);
    }

    public PostOutputDTO createHelpRequest(HelpRequestInputDTO helpRequestInputDTO, String username) {
        HelpRequest helpRequest = PostMapper.toEntity(helpRequestInputDTO);
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        helpRequest.setAuthor(author);
        Post post = postRepository.save(helpRequest);

        return PostMapper.toDTO(post);
    }

    public PostOutputDTO createActivity(ActivityInputDTO activityInputDTO, String username) {
        Activity activity = PostMapper.toEntity(activityInputDTO);
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        activity.setAuthor(author);
        Post post = postRepository.save(activity);

        return PostMapper.toDTO(post);
    }

    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Post not found"));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("Can't delete other users' posts");
        }

        postRepository.delete(post);
    }

    public PostOutputDTO markHelpFound(Long postId, List<Long> prizeRecipientIds, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RecordNotFoundException("Post not found"));

        if (!(post instanceof HelpRequest helpRequest)) {
            throw new IllegalArgumentException("Post is not a HelpRequest");
        }

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("Only the author can mark help as found");
        }

        Set<Long> friendIds = new HashSet<>();
        for (User friend : post.getAuthor().getFriends()) {
            friendIds.add(friend.getId());
        }

        for (Long recipientId : prizeRecipientIds) {
            if (!friendIds.contains(recipientId)) {
                throw new IllegalArgumentException("Can only award prizes to friends");
            }
        }

        helpRequest.setHelpFound(true);
        Prize prize = helpRequest.getHelpType().getPrize();

        for (Long recipientId : prizeRecipientIds) {
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + recipientId));
            recipient.getPrizes().add(prize);
            userRepository.save(recipient);
        }

        Post savedPost = postRepository.save(helpRequest);
        return PostMapper.toDTO(savedPost);
    }

    public PostOutputDTO joinActivity(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RecordNotFoundException("Post not found"));

        if (!(post instanceof Activity activity)) {
            throw new IllegalArgumentException("Post is not an Activity");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!activity.getAttendees().contains(user)) {
            activity.getAttendees().add(user);
        }

        if (!user.getAttendedActivities().contains(activity)) {
            user.getAttendedActivities().add(activity);
        }

        userRepository.save(user);
        Post savedPost = postRepository.save(activity);
        return PostMapper.toDTO(savedPost);
    }

    public void deletePostAsAdmin(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Post not found"));
        postRepository.delete(post);
    }
}
