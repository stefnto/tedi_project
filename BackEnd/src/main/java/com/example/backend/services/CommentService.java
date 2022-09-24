package com.example.backend.services;

import com.example.backend.models.Comment;

import java.util.List;

public interface CommentService {

    Comment saveComment(Comment comment);

    void addComment(Comment comment, Long id);

    List<Comment> getComments(Long id);

}
