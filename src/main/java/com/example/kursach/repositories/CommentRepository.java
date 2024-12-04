package com.example.kursach.repositories;

import com.example.kursach.models.Comment;
import com.example.kursach.models.Place;
import com.example.kursach.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByPlaceId(Long placeId);
}
