package com.giuzep89.helpinghandbackend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity extends Post {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    private LocalDateTime eventDate;

    @ManyToMany(mappedBy = "attendedActivities")
    private List<User> attendees = new ArrayList<>();

    // Constructors
    public Activity() {
        super();
    }

    public Activity(User author, String description, String location, ActivityType activityType, LocalDateTime eventDate) {
        super(author, description, location);
        this.activityType = activityType;
        this.eventDate = eventDate;
    }

    // Getters and Setters
    public ActivityType getActivityType() { return activityType; }
    public void setActivityType(ActivityType activityType) { this.activityType = activityType; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public List<User> getAttendees() { return attendees; }
    public void setAttendees(List<User> attendees) { this.attendees = attendees; }
}