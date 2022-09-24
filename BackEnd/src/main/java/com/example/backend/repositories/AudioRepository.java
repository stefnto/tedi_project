package com.example.backend.repositories;

import com.example.backend.models.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {
    @Query(value = "SELECT a.* FROM audio a, post_audios pa WHERE pa.post_id=:post_id AND a.id = pa.audios_id"
            , nativeQuery = true)
    Audio getAudioById(Long post_id);
}
