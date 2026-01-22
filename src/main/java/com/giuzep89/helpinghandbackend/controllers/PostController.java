package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.ActivityInputDTO;
import com.giuzep89.helpinghandbackend.dtos.HelpRequestInputDTO;
import com.giuzep89.helpinghandbackend.dtos.PostOutputDTO;
import com.giuzep89.helpinghandbackend.services.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostOutputDTO>> getAllPosts(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getAllPosts(username, page, size));
    }

    @PostMapping("/help-requests")
    public ResponseEntity<PostOutputDTO> createHelpRequest(
            @Valid @RequestBody HelpRequestInputDTO helpRequestInputDTO,
            @RequestParam String username) {
        PostOutputDTO created = postService.createHelpRequest(helpRequestInputDTO, username);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/activities")
    public ResponseEntity<PostOutputDTO> createActivity(
            @Valid @RequestBody ActivityInputDTO activityInputDTO,
            @RequestParam String username) {
        PostOutputDTO created = postService.createActivity(activityInputDTO, username);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestParam String username) {
        postService.deletePost(id, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/help-requests/{id}/help-found")
    public ResponseEntity<PostOutputDTO> markHelpFound(
            @PathVariable Long id,
            @RequestBody List<Long> prizeRecipientIds,
            @RequestParam String username) {
        return ResponseEntity.ok(postService.markHelpFound(id, prizeRecipientIds, username));
    }

    @PostMapping("/activities/{id}/join")
    public ResponseEntity<PostOutputDTO> joinActivity(
            @PathVariable Long id,
            @RequestParam String username) {
        return ResponseEntity.ok(postService.joinActivity(id, username));
    }
}