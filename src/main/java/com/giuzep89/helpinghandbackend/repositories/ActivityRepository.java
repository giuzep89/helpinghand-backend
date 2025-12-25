package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<HelpRequest, Long> {
}
