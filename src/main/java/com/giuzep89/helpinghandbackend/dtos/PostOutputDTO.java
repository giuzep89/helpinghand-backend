package com.giuzep89.helpinghandbackend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.giuzep89.helpinghandbackend.models.ActivityType;
import com.giuzep89.helpinghandbackend.models.HelpType;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PostOutputDTO {
    private Long id;
    private String displayTitle;  // To hold pre-made titles for either activities or helprequest (user doesn't write titles manually)
    private String description;
    private String location;
    private String authorUsername;
    private LocalDateTime createdAt;

    // To distinguish between helprequest and activity
    private String postType;

    // HelpRequest fields
    private HelpType helpType;
    private boolean helpFound;

    // Activity fields
    private ActivityType activityType;
    private LocalDateTime eventDate;
    private Integer currentParticipants;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public HelpType getHelpType() {
        return helpType;
    }

    public void setHelpType(HelpType helpType) {
        this.helpType = helpType;
    }

    public boolean isHelpFound() {
        return helpFound;
    }

    public void setHelpFound(boolean helpFound) {
        this.helpFound = helpFound;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }
}