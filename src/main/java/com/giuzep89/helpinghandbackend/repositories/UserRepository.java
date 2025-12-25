package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
