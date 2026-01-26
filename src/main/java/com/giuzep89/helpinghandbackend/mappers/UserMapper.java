package com.giuzep89.helpinghandbackend.mappers;

import com.giuzep89.helpinghandbackend.dtos.UserInputDTO;
import com.giuzep89.helpinghandbackend.dtos.UserOutputDTO;
import com.giuzep89.helpinghandbackend.models.User;

public class UserMapper {

    public static User toEntity(UserInputDTO inputDTO) {
        User entity = new User();

        entity.setUsername(inputDTO.getUsername());
        entity.setPassword(inputDTO.getPassword());
        entity.setEmail(inputDTO.getEmail());
        entity.setAge(inputDTO.getAge());
        entity.setLocation(inputDTO.getLocation());
        entity.setCompetencies(inputDTO.getCompetencies());

        return entity;
    }

    public static UserOutputDTO toDTO(User user) {
        UserOutputDTO outputDTO = new UserOutputDTO();

        outputDTO.setId(user.getId());
        outputDTO.setUsername(user.getUsername());
        outputDTO.setEmail(user.getEmail());
        outputDTO.setAge(user.getAge());
        outputDTO.setLocation(user.getLocation());
        outputDTO.setCompetencies(user.getCompetencies());
        outputDTO.setPrizes(user.getPrizes());
        outputDTO.setHasProfilePicture(user.getProfilePicture() != null);

        return outputDTO;
    }
}
