package com.giuzep89.helpinghandbackend.dtos;

import com.giuzep89.helpinghandbackend.models.ActivityType;
import com.giuzep89.helpinghandbackend.models.HelpType;
import java.time.LocalDateTime;

public class PostOutputDTO {
    private Long id;
    private String title;          // Will hold either the custom Activity title or the "Stock" Help title
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
    private int currentParticipants;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }
}