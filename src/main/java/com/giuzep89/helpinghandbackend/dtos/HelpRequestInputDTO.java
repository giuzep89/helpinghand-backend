package com.giuzep89.helpinghandbackend.dtos;

import com.giuzep89.helpinghandbackend.models.HelpType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HelpRequestInputDTO {

    @NotBlank(message = "Description is required")
    @Size(max = 300, message = "Description must be 300 characters or less")
    private String description;

    private String location;

    @NotNull(message = "Please select a help type")
    private HelpType helpType;


    // Getters and Setters
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

    public HelpType getHelpType() {
        return helpType;
    }

    public void setHelpType(HelpType helpType) {
        this.helpType = helpType;
    }
}