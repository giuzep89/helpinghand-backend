package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
