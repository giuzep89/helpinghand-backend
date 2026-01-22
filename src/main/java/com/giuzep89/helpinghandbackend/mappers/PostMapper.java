package com.giuzep89.helpinghandbackend.mappers;

import com.giuzep89.helpinghandbackend.dtos.ActivityInputDTO;
import com.giuzep89.helpinghandbackend.dtos.HelpRequestInputDTO;
import com.giuzep89.helpinghandbackend.dtos.PostOutputDTO;
import com.giuzep89.helpinghandbackend.models.Activity;
import com.giuzep89.helpinghandbackend.models.HelpRequest;
import com.giuzep89.helpinghandbackend.models.HelpType;
import com.giuzep89.helpinghandbackend.models.Post;

import java.time.LocalDateTime;

public class PostMapper {

    public static HelpRequest toEntity(HelpRequestInputDTO inputDTO) {
        HelpRequest entity = new HelpRequest();

        entity.setHelpType(inputDTO.getHelpType());
        entity.setLocation(inputDTO.getLocation());
        entity.setDescription(inputDTO.getDescription());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public static Activity toEntity(ActivityInputDTO inputDTO) {
        Activity entity = new Activity();
        entity.setActivityType(inputDTO.getActivityType());
        entity.setDescription(inputDTO.getDescription());
        entity.setLocation(inputDTO.getLocation());
        entity.setEventDate(inputDTO.getEventDate());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    // Joined OutputDTO for ease of data retrieval in the frontend
    public static PostOutputDTO toDTO(Post post) {
        PostOutputDTO dto = new PostOutputDTO();

        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setLocation(post.getLocation());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAuthorUsername(post.getAuthor().getUsername());

        // Post object is directly cast to HelpRequest and Activity thanks to pattern variable!
        if (post instanceof HelpRequest helpRequest) {

            dto.setPostType("HELP_REQUEST");

            dto.setHelpType(helpRequest.getHelpType());
            dto.setHelpFound(helpRequest.isHelpFound());

            if (helpRequest.getHelpType() == HelpType.COMPANY) {
                dto.setDisplayTitle("I'd love to have some company!");
            } else {
                dto.setDisplayTitle("I need some help with " + helpRequest.getHelpType().getDisplayName() + "!");
            }
        } else if (post instanceof Activity activity) {

            dto.setPostType("ACTIVITY");

            dto.setActivityType(activity.getActivityType());
            dto.setEventDate(activity.getEventDate());
            dto.setCurrentParticipants(activity.getAttendees().size());
            dto.setDisplayTitle("Going for a " + activity.getActivityType().getDisplayName() + " activity: anyone joining?");
        }

        return dto;
    }

}
