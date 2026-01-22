package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // to retrieve User with an exact query
    public Optional<User> findByUsername(String username);

    // not case-sensitive, to search for friends
    public List<User> findByUsernameContainingIgnoreCase(String query);
}
