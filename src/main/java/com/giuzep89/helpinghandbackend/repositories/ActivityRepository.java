package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
