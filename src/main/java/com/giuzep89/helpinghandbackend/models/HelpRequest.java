package com.giuzep89.helpinghandbackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "help_requests")
public class HelpRequest extends Post {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpType helpType;
    private boolean helpFound = false;

    // Constructors
    public HelpRequest() {
        super();
    }

    public HelpRequest(User author, String description, String location, HelpType helpType) {
        super(author, description, location);
        this.helpType = helpType;
    }

    // Getters and Setters
    public HelpType getHelpType() { return helpType; }
    public void setHelpType(HelpType helpType) { this.helpType = helpType; }

    public boolean isHelpFound() { return helpFound; }
    public void setHelpFound(boolean helpFound) { this.helpFound = helpFound; }
}