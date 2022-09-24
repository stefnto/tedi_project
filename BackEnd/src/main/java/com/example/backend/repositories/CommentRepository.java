package com.example.backend.repositories;

import com.example.backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c.* FROM post_comments pc, comment c WHERE pc.post_id=:id && c.id = pc.comments_id",
            nativeQuery = true)
    List<Comment> allCommentsofPost(@Param("id") Long id);
}
