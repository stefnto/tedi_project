package com.example.backend.services;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.models.Audio;
import com.example.backend.models.Image;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.models.Post;
import com.example.backend.models.PostDTO;
import com.example.backend.models.Video;
import com.example.backend.repositories.LikeRepository;
import com.example.backend.repositories.MemberRepository;
import com.example.backend.repositories.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRep;
    private final MemberRepository memberRep;
    private final LikeRepository likeRep;

    private final FriendServiceImpl friendService;
    private final NotificationServiceImpl notificationService;
    private final ImageServiceImpl imageService;
    private final VideoServiceImpl videoService;
    private final AudioServiceImpl audioService;

    // helper method, called from addPost
    @Override
    public Post savePost(Post post) {
        log.info("Saving new post {} to the db", post.getId());
        return postRep.save(post);
    }

    @Override
    public void addPost(String email, Post post) {
        Post saved_post = this.savePost(post);
        Member member = memberRep.findByEmail(email);
        member.getPosts().add(saved_post);
    }


    @Override
    public List<PostDTO> getAllPosts(String email) {

        Member loggedInMember = memberRep.findByEmail(email);

        List<MemberInfo> friendsArray = friendService.getFriends(email);
        
        List<PostDTO> postsToDisplay = new ArrayList<>();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Add friends' posts and then member's (email) posts
        for (MemberInfo memberInfo : friendsArray){
            for (Object[] userPost : postRep.allPostsOfUser(memberInfo.getId())){
                try {
                    postsToDisplay.add(
                        new PostDTO(
                            (Long) userPost[0],
                            dateFormatter.parse(userPost[1].toString()),
                            userPost[2].toString(),
                            userPost[3].toString(),
                            userPost[4].toString()
                        )
                    );
                } catch (ParseException e) {
                    log.error("Error adding logged in users' friends' posts", e);
                }

            }
        }

        for (Object[] userPost : postRep.allPostsOfUser(loggedInMember.getId())){
            try {
                postsToDisplay.add(
                    new PostDTO(
                        (Long) userPost[0],
                        dateFormatter.parse(userPost[1].toString()),
                        userPost[2].toString(),
                        userPost[3].toString(),
                        userPost[4].toString()
                    )
                );
            } catch (ParseException e) {
                log.error("Error adding logged in users' posts", e);
            }

        }

        return postsToDisplay;
    }

    @Override
    public List<Post> getRecommendedPosts(String email) {

        List<MemberInfo> friends = friendService.getFriends(email);
        List<Object[]> likedPosts = postRep.getAllLikedPosts(email);

        // if member has no friend there can't be post recommendations
        if (!friends.isEmpty()) {

            List<Post> recommendedPosts = new ArrayList<>();

            // postIds exists so as not to put duplicates in recommendedPosts list
            List<Long> postIds = new ArrayList<>();

            System.out.println(friends.size());

            for (Object[] likedPost : likedPosts) {
                Long memberIdThatLikedPost = (Long) likedPost[0];
                Long postId = (Long) likedPost[4];
                Long memberIdThatMadePost = (Long) likedPost[5];
                
                MemberInfo memberInfoThatLikedPost = new MemberInfo(memberIdThatLikedPost, likedPost[1].toString(), likedPost[2].toString(), likedPost[3].toString(), null);
                MemberInfo memberInfoThatMadePost = new MemberInfo(memberIdThatMadePost, likedPost[8].toString(), likedPost[9].toString(), likedPost[6].toString(), null);
                System.out.println(memberInfoThatLikedPost);

                if (!postIds.contains(postId)) {
                    if (friends.contains(memberInfoThatLikedPost) && !friends.contains(memberInfoThatMadePost)) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Post post = new Post(
                                postId,
                                likedPost[10].toString(),
                                null,
                                formatter.parse(likedPost[7].toString()),
                                likedPost[8].toString(),
                                likedPost[9].toString(),
                                null, null, null
                            );
                            System.out.println(post);
                            
                            postIds.add(postId);
                            recommendedPosts.add(post);
                        } catch (ParseException e) {
                            log.error("Error adding post to recommended list", e);
                        }
                    }
                }
            }
            return recommendedPosts;
        } else
            return null;
    }

    @Override
    public Post getPostById(Long post_id) {
        return postRep.getReferenceById(post_id);
    }

    @Override
    public String getPostEmail(Long post_id) {
        return postRep.getPostEmail(post_id);
    }

    @Override
    public void likePost(String email, Long post_id) {

        Member member = memberRep.findByEmail(email);

        likeRep.likePost(member.getId(), post_id);

        // set the notification for the like
        String post_email = this.getPostEmail(post_id);
        if (!post_email.equals(email)) {
            String text = member.getName() + " " + member.getSurname() + " liked your post with id = " + post_id;
            notificationService.saveNotification(text, post_email);
        }
    }

    @Override
    public void unLikePost(String email, Long post_id) {

        Member member = memberRep.findByEmail(email);

        likeRep.unlikePost(member.getId(), post_id);
    }

    @Override
    public List<BigInteger> likedPosts(List<Long> posts_ids, String email) {

        Member member = memberRep.findByEmail(email);

        List<BigInteger> list = new ArrayList<>();
        posts_ids.forEach(id -> list.add(likeRep.isLiked(member.getId(), id)));
        return list;
    }

    @Override
    public List<Integer> numberOfLikes(List<Long> posts_ids) {
        List<Integer> list = new ArrayList<>();
        posts_ids.forEach(id -> list.add(likeRep.getNumOfLikes(id)));
        return list;
    }

    @Override
    public List<Image> getImages(List<Long> post_ids) {
        List<Image> list = new ArrayList<>();
        post_ids.forEach(id -> list.add(imageService.getImage(id)));
        return list;
    }

    @Override
    public void addImageToPost(Long post_id, MultipartFile file) throws IOException {
        Image img = new Image(file.getOriginalFilename(), file.getContentType(),
                imageService.compressBytes(file.getBytes()));
        Image saved_image = imageService.saveImage(img);
        getPostById(post_id).getImages().add(saved_image);
    }

    @Override
    public void addVideoToPost(Long post_id, MultipartFile file) throws IOException{
        System.out.println("length of video = " + file.getBytes().length);
        Video vdo = new Video(file.getOriginalFilename(), file.getContentType(),
                imageService.compressBytes(file.getBytes()));
        Video saved_video = videoService.saveVideo(vdo);
        getPostById(post_id).getVideos().add(saved_video);
    }

    @Override
    public List<Video> getVideos(List<Long> post_ids){
        List<Video> list = new ArrayList<>();
        post_ids.forEach(id -> list.add(videoService.getVideo(id)));
        return list;
    }

    @Override
    public void addAudioToPost(Long post_id, MultipartFile file) throws IOException{
        Audio audio = new Audio(file.getOriginalFilename(), file.getContentType(),
                imageService.compressBytes(file.getBytes()));
        Audio saved_audio = audioService.saveAudio(audio);
        getPostById(post_id).getAudios().add(saved_audio);
    }

    @Override
    public List<Audio> getAudios(List<Long> post_ids){
        List<Audio> list = new ArrayList<>();
        post_ids.forEach(id -> list.add(audioService.getAudio(id)));
        return list;
    }
}
