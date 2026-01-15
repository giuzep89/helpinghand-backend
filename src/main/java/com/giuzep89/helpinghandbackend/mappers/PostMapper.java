package com.giuzep89.helpinghandbackend.mappers;

import com.giuzep89.helpinghandbackend.dtos.ActivityInputDTO;
import com.giuzep89.helpinghandbackend.dtos.HelpRequestInputDTO;
import com.giuzep89.helpinghandbackend.dtos.PostOutputDTO;
import com.giuzep89.helpinghandbackend.models.Activity;
import com.giuzep89.helpinghandbackend.models.HelpRequest;
import com.giuzep89.helpinghandbackend.models.HelpType;
import com.giuzep89.helpinghandbackend.models.Post;

public class PostMapper {

    public static HelpRequest toEntity(HelpRequestInputDTO inputDTO) {
        HelpRequest entity = new HelpRequest();

        entity.setHelpType(inputDTO.getHelpType());
        entity.setLocation(inputDTO.getLocation());
        entity.setDescription(inputDTO.getDescription());

        return entity;
    }

    public static Activity toEntity(ActivityInputDTO inputDTO) {
        Activity entity = new Activity();
        entity.setActivityType(inputDTO.getActivityType());
        entity.setDescription(inputDTO.getDescription());
        entity.setLocation(inputDTO.getLocation());
        entity.setEventDate(inputDTO.getEventDate());

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

        if (post instanceof HelpRequest) {
            HelpRequest helpRequest = (HelpRequest) post;

            dto.setPostType("HELP_REQUEST");

            dto.setHelpType(helpRequest.getHelpType());
            dto.setHelpFound(helpRequest.isHelpFound());

            if (helpRequest.getHelpType() == HelpType.COMPANY) {
                dto.setDisplayTitle("I'd love to have some company!");
            } else {
                dto.setDisplayTitle("I need some help with " + helpRequest.getHelpType().getDisplayName() + "!");
            }
        } else if (post instanceof Activity) {
            Activity activity = (Activity) post;

            dto.setPostType("ACTIVITY");

            dto.setActivityType(activity.getActivityType());
            dto.setEventDate(activity.getEventDate());
            dto.setDisplayTitle(activity.getActivityType() + " activity: anyone joining?");
        }

        return dto;
    }

}
