package com.example.backend.services;

import com.example.backend.models.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface PostService {

    // save post into DB
    Post savePost(Post post);

    // add the post to the user that made it
    void addPost(String email, Post post);

    // PostDTO is Post without images,videos,audios,
    // but recommendedPosts returns null to those fields to reduce size transfer to the client
    List<PostDTO> getAllPosts(String email);

    List<Post> getRecommendedPosts(String email);


    Post getPostById(Long post_id);

    String getPostEmail(Long post_id);

    // likes the post with post_id from member
    void likePost(String email, Long id);

    // unlikes the post with post_id from member
    void unLikePost(String email, Long id);

    // returns a list of 1 or 0, 1 meaning member has liked specific post, 0 meaning not liked
    List<BigInteger> likedPosts(List<Long> posts_ids, String email);

    // returns a list with the number of likes of each post (in the the sequence they exist in posts_ids list)
    List<Integer> numberOfLikes(List<Long> posts_ids);

    // image,video, audio setters/getters
    void addImageToPost(Long id, MultipartFile file) throws IOException;

    List<Image> getImages(List<Long> post_ids);

    void addVideoToPost(Long id, MultipartFile file) throws IOException;

    List<Video> getVideos(List<Long> post_ids);

    void addAudioToPost(Long id, MultipartFile file) throws IOException;

    List<Audio> getAudios(List<Long> post_ids);

}
