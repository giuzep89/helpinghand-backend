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
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.PostRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostOutputDTO> getAllPosts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        List<PostOutputDTO> postsToRetrieve = new ArrayList<>();
        List<User> allUsersOnFeed = new ArrayList<>(user.getFriends());
        allUsersOnFeed.add(user);

        for (User userOnFeed : allUsersOnFeed) {
            List<Post> allPosts = postRepository.findAllByAuthorId(userOnFeed.getId());
            for (Post post : allPosts) {
                postsToRetrieve.add(PostMapper.toDTO(post));
            }
        }
        return postsToRetrieve;
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









}
