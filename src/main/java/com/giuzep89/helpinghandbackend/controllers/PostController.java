package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.ActivityInputDTO;
import com.giuzep89.helpinghandbackend.dtos.HelpRequestInputDTO;
import com.giuzep89.helpinghandbackend.dtos.PostOutputDTO;
import com.giuzep89.helpinghandbackend.services.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getAllPosts(userDetails.getUsername(), page, size));
    }

    @PostMapping("/help-requests")
    public ResponseEntity<PostOutputDTO> createHelpRequest(
            @Valid @RequestBody HelpRequestInputDTO helpRequestInputDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostOutputDTO created = postService.createHelpRequest(helpRequestInputDTO, userDetails.getUsername());
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
            @AuthenticationPrincipal UserDetails userDetails) {
        PostOutputDTO created = postService.createActivity(activityInputDTO, userDetails.getUsername());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/help-requests/{id}/help-found")
    public ResponseEntity<PostOutputDTO> markHelpFound(
            @PathVariable Long id,
            @RequestBody List<Long> prizeRecipientIds,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.markHelpFound(id, prizeRecipientIds, userDetails.getUsername()));
    }

    @PostMapping("/activities/{id}/join")
    public ResponseEntity<PostOutputDTO> joinActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.joinActivity(id, userDetails.getUsername()));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePostAsAdmin(@PathVariable Long id) {
        postService.deletePostAsAdmin(id);
        return ResponseEntity.noContent().build();
    }
}