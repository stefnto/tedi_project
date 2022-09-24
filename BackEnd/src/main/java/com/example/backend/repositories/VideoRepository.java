package com.example.backend.repositories;

import com.example.backend.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query(value = "SELECT v.* FROM video v, post_videos pv WHERE pv.post_id=:post_id AND v.id = pv.videos_id"
            , nativeQuery = true)
    Video getVideoById(Long post_id);
}

