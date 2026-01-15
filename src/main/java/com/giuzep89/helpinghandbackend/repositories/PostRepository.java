package com.giuzep89.helpinghandbackend.repositories;

import com.giuzep89.helpinghandbackend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> findAllByAuthorId(Long userId);

}
