package com.giuzep89.helpinghandbackend.models;

public enum ActivityType {
    SPORTS("sports"),
    CULTURE("cultural"),
    VOLUNTEERING("volunteering"),
    SOCIAL("social"),
    EDUCATIONAL("educational");

    private final String activityType;

    ActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityType() {
        return activityType;
    }
}