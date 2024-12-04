package com.example.kursach.services;

import com.example.kursach.models.Comment;
import com.example.kursach.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> allComments() {
        return commentRepository.findAll();
    }


    public Comment getCommentById(Long id) {
        Optional<Comment> foundedComment = commentRepository.findById(id);
        return foundedComment.orElse(null);
    }

    public List<Comment> getCommentsByPlaceId(Long placeId) {
        return commentRepository.getCommentsByPlaceId(placeId);
    }

    @Transactional
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long id, String newText) {
        Comment updatedComment = getCommentById(id);
        updatedComment.setText(newText);
        commentRepository.save(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
