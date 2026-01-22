package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.UserOutputDTO;
import com.giuzep89.helpinghandbackend.dtos.UserUpdateDTO;
import com.giuzep89.helpinghandbackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // TODO Add POST /users/register endpoint (Spring Security - password encoding)
    // TODO Add POST /users/login endpoint (Spring Security - authentication)

    @GetMapping
    public ResponseEntity<List<UserOutputDTO>> searchUsers(
            @RequestParam String q,
            @RequestParam String username) {
        return ResponseEntity.ok(userService.searchUsers(q, username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserOutputDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserOutputDTO> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateDTO updateDTO) {
        return ResponseEntity.ok(userService.updateUser(username, updateDTO));
    }

    @GetMapping("/{username}/friends")
    public ResponseEntity<List<UserOutputDTO>> getFriends(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFriends(username));
    }

    @PostMapping("/{username}/friends")
    public ResponseEntity<UserOutputDTO> addFriend(
            @PathVariable String username,
            @RequestBody Long friendId) {
        return ResponseEntity.ok(userService.addFriend(username, friendId));
    }

    @DeleteMapping("/{username}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable String username,
            @PathVariable Long friendId) {
        userService.removeFriend(username, friendId);
        return ResponseEntity.noContent().build();
    }
}
