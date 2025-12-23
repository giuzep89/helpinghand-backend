package com.giuzep89.helpinghandbackend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "posts")
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User author;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String location;

    // Constructors
    public Post() {}

    public Post(User author, String description, String location) {
        this.author = author;
        this.description = description;
        this.location = location;
        this.createdAt = LocalDateTime.now();
    }



}
