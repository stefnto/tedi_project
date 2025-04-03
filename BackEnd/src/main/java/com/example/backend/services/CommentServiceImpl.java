package com.example.backend.services;

import com.example.backend.models.Comment;
import com.example.backend.models.Post;
import com.example.backend.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRep;
    private final PostServiceImpl postService;
    private final NotificationService notificationService;

    @Override
    public Comment saveComment(Comment comment) {
        return commentRep.save(comment);
    }

    @Override
    public void addComment(Comment comment, Long id){
        Comment saved_comment = saveComment(comment);
        Post post = this.postService.getPostById(id);
        post.getComments().add(saved_comment);

        // if the commenter isn't the member that made the post
        // set the notification for the comment
        if (!saved_comment.getName().equals(post.getPost_name()) || !saved_comment.getSurname().equals(post.getPost_surname())){
            String post_email = postService.getPostEmail(id);
            String text = saved_comment.getName() + " " + saved_comment.getSurname() + " commented on your post with id = " + id;
            notificationService.saveNotification(text, post_email);
        }
    }

    @Override
    public List<Comment> getComments(Long id) {
        return commentRep.allCommentsofPost(id);
    }

}
