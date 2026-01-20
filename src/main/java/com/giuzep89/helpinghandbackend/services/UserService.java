package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.UserInputDTO;
import com.giuzep89.helpinghandbackend.dtos.UserOutputDTO;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.mappers.UserMapper;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO Add registerUser method with password encoding (Spring Security)
    // TODO Implement UserDetailsService.loadUserByUsername for authentication
    // TODO Add authority/role management methods

    public UserOutputDTO getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        return UserMapper.toDTO(user);
    }

    public UserOutputDTO updateUser(String username, UserInputDTO inputDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        // Update only non-security fields
        if (inputDTO.getAge() != 0) {
            user.setAge(inputDTO.getAge());
        }
        if (inputDTO.getLocation() != null) {
            user.setLocation(inputDTO.getLocation());
        }
        if (inputDTO.getCompetencies() != null) {
            user.setCompetencies(inputDTO.getCompetencies());
        }

        user = userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public List<UserOutputDTO> getFriends(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        return user.getFriends().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserOutputDTO addFriend(String username, Long friendId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RecordNotFoundException("Friend not found"));

        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
            userRepository.save(user);
        }

        return UserMapper.toDTO(friend);
    }

    public void removeFriend(String username, Long friendId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RecordNotFoundException("Friend not found"));

        user.getFriends().remove(friend);
        userRepository.save(user);
    }
}