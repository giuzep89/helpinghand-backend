package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.UserOutputDTO;
import com.giuzep89.helpinghandbackend.dtos.UserUpdateDTO;
import com.giuzep89.helpinghandbackend.exceptions.UnauthorizedException;
import com.giuzep89.helpinghandbackend.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private void verifyUserAccess(String pathUsername, UserDetails userDetails) {
        if (!pathUsername.equals(userDetails.getUsername())) {
            throw new UnauthorizedException("You can only access your own resources");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserOutputDTO>> searchUsers(
            @RequestParam String q,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.searchUsers(q, userDetails.getUsername()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserOutputDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserOutputDTO> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateDTO updateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        verifyUserAccess(username, userDetails);
        return ResponseEntity.ok(userService.updateUser(username, updateDTO));
    }

    @GetMapping("/{username}/friends")
    public ResponseEntity<List<UserOutputDTO>> getFriends(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        verifyUserAccess(username, userDetails);
        return ResponseEntity.ok(userService.getFriends(username));
    }

    @PostMapping("/{username}/friends")
    public ResponseEntity<UserOutputDTO> addFriend(
            @PathVariable String username,
            @RequestBody Long friendId,
            @AuthenticationPrincipal UserDetails userDetails) {
        verifyUserAccess(username, userDetails);
        return ResponseEntity.ok(userService.addFriend(username, friendId));
    }

    @DeleteMapping("/{username}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable String username,
            @PathVariable Long friendId,
            @AuthenticationPrincipal UserDetails userDetails) {
        verifyUserAccess(username, userDetails);
        userService.removeFriend(username, friendId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{username}/profile-picture")
    public ResponseEntity<UserOutputDTO> uploadProfilePicture(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        verifyUserAccess(username, userDetails);
        return ResponseEntity.ok(userService.uploadProfilePicture(username, file));
    }

    @GetMapping("/{username}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        byte[] imageData = userService.getProfilePicture(username);
        String contentType = userService.getProfilePictureType(username);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(imageData);
    }

    @DeleteMapping("/{username}/profile-picture")
    public ResponseEntity<Void> deleteProfilePicture(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        verifyUserAccess(username, userDetails);
        userService.deleteProfilePicture(username);
        return ResponseEntity.noContent().build();
    }
}
