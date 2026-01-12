package com.giuzep89.helpinghandbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserInputDTO {

    @NotBlank(message = "Please fill out username")
    private String username;
    @NotBlank(message = "Please fill out email")
    @Email
    private String email;
    @NotBlank(message = "Please choose a password")
    @Size(min = 8, message = "The password must be at least 8 characters long")
    private String password;
    private Short age;
    private String location;
    private String competencies;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
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
