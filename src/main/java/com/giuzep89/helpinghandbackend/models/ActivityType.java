package com.giuzep89.helpinghandbackend.models;

public enum ActivityType {
    SPORTS("Sports"),
    CULTURE("Culture"),
    VOLUNTEERING("Volunteering"),
    SOCIAL("Social"),
    EDUCATIONAL("Educational");

    private final String activityType;

    ActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityType() {
        return activityType;
    }
}