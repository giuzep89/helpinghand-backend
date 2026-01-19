package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.Chat;
import com.giuzep89.helpinghandbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE (c.userOne = :u1 AND c.userTwo = :u2) OR (c.userOne = :u2 AND c.userTwo = :u1)")
    Optional<Chat> findChatBetweenUsers(@Param("u1") User u1, @Param("u2") User u2);
}
