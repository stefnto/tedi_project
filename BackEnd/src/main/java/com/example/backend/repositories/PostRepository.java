package com.example.backend.repositories;

import com.example.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p.* FROM member_posts mp, post p " +
            "WHERE mp.member_id = :member_id AND p.id = mp.posts_id",
            nativeQuery = true)
    List<Object[]> allPostsOfUser(Long member_id);

    // gets information on who has like which post, but with the condition that post creator isn't member with email given
    // member_id_that_liked, post information..
    @Query(value = "SELECT m1.id AS member_that_liked_post, m1.name, m1.surname, m1.email AS email_of_member_that_liked_post, p.id as post_id, mp.member_id AS member_that_made_post, m2.email AS email_of_member_that_made_post, p.date as post_date, p.post_name, p.post_surname, p.text as p_text " +
            "FROM likes l, post p, member_posts mp, member m1, member m2 WHERE l.post_id = p.id AND mp.posts_id = p.id AND l.member_id = m1.id AND m1.id <> mp.member_id AND mp.member_id = m2.id AND m1.email <> :email AND p.id  NOT IN " +
            "               (Select mp.posts_id FROM  member_posts mp, member m WHERE mp.member_id = m.id and m.email = :email);",
            nativeQuery = true)
    List<Object[]> getAllLikedPosts(String email);

    @Query(value = "Select m.email FROM member_posts mp, member m " +
            "WHERE mp.posts_id = :post_id AND mp.member_id = m.id",
            nativeQuery = true)
    String getPostEmail(Long post_id);
}
