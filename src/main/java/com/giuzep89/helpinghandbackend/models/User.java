package com.giuzep89.helpinghandbackend.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    private Short age;
    private String location;

    @Column(length = 1000)
    private String competencies; // it's mostly called "things I can do" in the frontend

    @ElementCollection(targetClass = Prize.class)
    @CollectionTable(name = "user_prizes", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private List<Prize> prizes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    @OneToMany(mappedBy = "userOne")
    private List<Chat> initiatedChats  = new ArrayList<>();

    @OneToMany(mappedBy = "userTwo")
    private List<Chat> receivedChats  = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_activities", // The name of the middle table in the DB
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<Activity> attendedActivities = new ArrayList<>();

// TODO Add list of Authorities + authority methods



    // Getters and setters
    public Long getId() {
        return id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Chat> getInitiatedChats() {
        return initiatedChats;
    }

    public void setInitiatedChats(List<Chat> initiatedChats) {
        this.initiatedChats = initiatedChats;
    }

    public List<Chat> getReceivedChats() {
        return receivedChats;
    }

    public void setReceivedChats(List<Chat> receivedChats) {
        this.receivedChats = receivedChats;
    }

    public List<Activity> getAttendedActivities() {
        return attendedActivities;
    }

    public void setAttendedActivities(List<Activity> attendedActivities) {
        this.attendedActivities = attendedActivities;
    }
}
