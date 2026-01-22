package com.giuzep89.helpinghandbackend.dtos;

public class UserUpdateDTO {

    private Short age;
    private String location;
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