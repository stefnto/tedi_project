package com.example.backend.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.models.Audio;
import com.example.backend.models.Comment;
import com.example.backend.models.Image;
import com.example.backend.models.Post;
import com.example.backend.models.PostDTO;
import com.example.backend.models.Video;
import com.example.backend.services.CommentServiceImpl;
import com.example.backend.services.ImageServiceImpl;
import com.example.backend.services.PostServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/post")
public class PostController {
    private final PostServiceImpl postService;
    private final ImageServiceImpl imageService;
    private final CommentServiceImpl commentService;

    @PostMapping("/addtomember/{email}")
    public ResponseEntity<?>addPostToMember(@RequestBody Post post, @PathVariable String email){
        postService.addPost(email, post);
        return ResponseEntity.status(201).body("Post made");
    }

    @GetMapping("/list/all/{email}")
    public ResponseEntity<List<PostDTO>> getAllPostsOfMember(@PathVariable String email) {
        return ResponseEntity.ok().body(postService.getAllPosts(email));
    }

    @GetMapping("/list/recommended/{email}")
    public ResponseEntity<List<Post>> getRecommendedNonFriendPosts(@PathVariable String email){
        return ResponseEntity.ok().body(postService.getRecommendedPosts(email));
    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<?> addCommentToPost(@RequestBody Comment comment, @PathVariable Long id){
        commentService.addComment(comment, id);
        return ResponseEntity.status(201).body("Comment made");
    }

    @GetMapping("/comments/list/{id}")
    public ResponseEntity<List<Comment>> getCommentsOfPost(@PathVariable Long id){
        return ResponseEntity.ok().body(commentService.getComments(id));
    }


    @PostMapping("/like")
    public ResponseEntity<?>doLike(@RequestHeader Long post_id, @RequestBody String email){
        postService.likePost(email, post_id);
        return ResponseEntity.status(201).body("Like sent");
    }

    @PostMapping("/unlike")
    public ResponseEntity<?>doUnLike(@RequestHeader Long post_id, @RequestBody String email){
        postService.unLikePost(email, post_id);
        return ResponseEntity.status(201).body("Like cancelled");
    }

    @GetMapping("/isLiked")
    public ResponseEntity<?>isLiked(@RequestHeader List<Long> array, @RequestHeader String email){
        return ResponseEntity.ok().body(postService.likedPosts(array, email));
    }

    @GetMapping("/likes_number")
    public ResponseEntity<List<Integer>>getMembersLiked(@RequestHeader List<Long> array){
        return ResponseEntity.ok().body(postService.numberOfLikes(array));
    }

    @PostMapping("/upload_image")
    public ResponseEntity.BodyBuilder uploadImage(@RequestParam("imageFile") MultipartFile file, @RequestHeader Long post_id) throws IOException {
        postService.addImageToPost(post_id, file);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping( "/get_image/{post_id}")
    public ResponseEntity<?> getImageFromPost(@PathVariable Long post_id){
        return ResponseEntity.ok().body(imageService.getImage(post_id));
    }

    @GetMapping("/get_images")
    public ResponseEntity<List<Image>> getAllImages(@RequestHeader List<Long> array){
        return ResponseEntity.ok().body(postService.getImages(array));
    }



    @PostMapping("/upload_video")
    public ResponseEntity.BodyBuilder uploadVideo(@RequestParam("videoFile") MultipartFile file, @RequestHeader Long post_id) throws IOException {
        postService.addVideoToPost(post_id, file);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping("/get_videos")
    public ResponseEntity<List<Video>> getAllVideos(@RequestHeader List<Long> array){
        return ResponseEntity.ok().body(postService.getVideos(array));
    }

    @PostMapping("/upload_audio")
    public ResponseEntity.BodyBuilder uploadAudio(@RequestParam("audioFile") MultipartFile file, @RequestHeader Long post_id) throws IOException {
        postService.addAudioToPost(post_id, file);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping("/get_audios")
    public ResponseEntity<List<Audio>> getAllAudios(@RequestHeader List<Long> array){
        return ResponseEntity.ok().body(postService.getAudios(array));
    }
}
