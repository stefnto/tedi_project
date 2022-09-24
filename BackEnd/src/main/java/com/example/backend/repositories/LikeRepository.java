package com.example.backend.repositories;

import com.example.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface LikeRepository extends JpaRepository<Post, Long>{

    @Modifying
    @Query(value = "INSERT INTO likes " +
            "VALUES(:member_id, :post_id)",  nativeQuery = true)
    void likePost(Long member_id, Long post_id);

    @Modifying
    @Query(value = "DELETE FROM likes " +
            "WHERE likes.member_id = :member_id AND likes.post_id = :post_id",
            nativeQuery = true)
    void unlikePost(Long member_id, Long post_id);

    @Query(value = "SELECT CASE WHEN COUNT(*)>0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM likes WHERE member_id = :member_id AND post_id = :post_id",
            nativeQuery = true)
    BigInteger isLiked(Long member_id, Long post_id);

    @Query(value = "SELECT COUNT(*) FROM likes WHERE post_id = :post_id", nativeQuery = true)
    Integer getNumOfLikes(Long post_id);
}
