package com.giuzep89.helpinghandbackend.models;

public enum ActivityType {
    SPORTS("Sports"),
    CULTURE("Culture"),
    VOLUNTEERING("Volunteering"),
    SOCIAL("Social"),
    LEARNING("Learning");

    private final String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}