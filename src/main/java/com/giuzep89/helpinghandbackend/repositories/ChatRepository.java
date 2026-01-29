package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.Chat;
import com.giuzep89.helpinghandbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByUserOneAndUserTwo(User userOne, User userTwo);

    List<Chat> findByUserOneOrUserTwo(User userOne, User userTwo);
}
