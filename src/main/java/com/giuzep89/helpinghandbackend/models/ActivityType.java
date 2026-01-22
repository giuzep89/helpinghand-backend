package com.giuzep89.helpinghandbackend.models;

public enum ActivityType {
    SPORTS("sports"),
    CULTURE("cultural"),
    VOLUNTEERING("volunteering"),
    SOCIAL("social"),
    EDUCATIONAL("educational");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}