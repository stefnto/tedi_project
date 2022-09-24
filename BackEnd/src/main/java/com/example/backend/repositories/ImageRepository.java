package com.example.backend.repositories;

import com.example.backend.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT i.* FROM image i, post_images pi WHERE pi.post_id=:post_id AND i.id = pi.images_id"
            , nativeQuery = true)
    Image getImageById(Long post_id);
}
