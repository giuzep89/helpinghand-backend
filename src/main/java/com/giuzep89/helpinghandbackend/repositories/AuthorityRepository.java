package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
