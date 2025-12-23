package com.giuzep89.helpinghandbackend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity extends Post {

    private ActivityType activityType;

    private LocalDateTime eventDate;

    // We use a List of Users for attendees
    // We'll handle the @ManyToMany annotation later when you're ready!
    @ManyToMany
    private List<User> attendees = new ArrayList<>();

    public Activity() {
        super();
    }

    public Activity(User author, String description, String location,
                    ActivityType activityType, LocalDateTime eventDate) {
        super(author, description, location);
        this.activityType = activityType;
        this.eventDate = eventDate;
    }
}