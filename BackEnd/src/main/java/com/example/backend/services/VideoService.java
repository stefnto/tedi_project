package com.example.backend.services;

import com.example.backend.models.Video;

public interface VideoService {
    Video saveVideo(Video video);

    Video getVideo(Long post_id);
}
