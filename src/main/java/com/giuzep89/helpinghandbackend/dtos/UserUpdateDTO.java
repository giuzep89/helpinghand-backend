package com.giuzep89.helpinghandbackend.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @Positive(message = "Age must be a positive number")
    @Max(value = 150, message = "Age must be 150 or less")
    private Short age;

    @Size(max = 100, message = "Location must be 100 characters or less")
    private String location;

    @Size(max = 1000, message = "Competencies must be 1000 characters or less")
    private String competencies;

    // Getters and setters
    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompetencies() {
        return competencies;
    }

    public void setCompetencies(String competencies) {
        this.competencies = competencies;
    }
}