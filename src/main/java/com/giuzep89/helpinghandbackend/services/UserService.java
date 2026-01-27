package com.giuzep89.helpinghandbackend.services;

import com.giuzep89.helpinghandbackend.dtos.UserOutputDTO;
import com.giuzep89.helpinghandbackend.dtos.UserUpdateDTO;
import com.giuzep89.helpinghandbackend.exceptions.InvalidFileException;
import com.giuzep89.helpinghandbackend.exceptions.RecordNotFoundException;
import com.giuzep89.helpinghandbackend.mappers.UserMapper;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserOutputDTO> searchUsers(String query, String currentUsername) {
        return userRepository.findByUsernameContainingIgnoreCase(query).stream()
                .filter(user -> !user.getUsername().equals(currentUsername))
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserOutputDTO getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        return UserMapper.toDTO(user);
    }

    public UserOutputDTO updateUser(String username, UserUpdateDTO updateDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (updateDTO.getAge() != null) {
            user.setAge(updateDTO.getAge());
        }
        if (updateDTO.getLocation() != null) {
            user.setLocation(updateDTO.getLocation());
        }
        if (updateDTO.getCompetencies() != null) {
            user.setCompetencies(updateDTO.getCompetencies());
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

    // File upload functionalities - Profile picture
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // Transactional is here to avoid confusing errors that might occur during the handling of the upload/download
    // of the file. Adds extra integrity to the process
    @Transactional
    public UserOutputDTO uploadProfilePicture(String username, MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("File size exceeds maximum allowed size of 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidFileException("Only JPEG and PNG images are allowed");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        try {
            user.setProfilePicture(file.getBytes());
            user.setProfilePictureType(contentType);
            user = userRepository.save(user);
            return UserMapper.toDTO(user);
        } catch (IOException e) {
            throw new InvalidFileException("Failed to process file");
        }
    }

    @Transactional(readOnly = true)
    public byte[] getProfilePicture(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (user.getProfilePicture() == null) {
            throw new RecordNotFoundException("Profile picture not found");
        }

        return user.getProfilePicture();
    }

    @Transactional(readOnly = true)
    public String getProfilePictureType(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        return user.getProfilePictureType();
    }

    @Transactional
    public void deleteProfilePicture(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        user.setProfilePicture(null);
        user.setProfilePictureType(null);
        userRepository.save(user);
    }
}