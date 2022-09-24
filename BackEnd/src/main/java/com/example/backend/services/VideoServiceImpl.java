package com.example.backend.services;


import com.example.backend.models.Video;
import com.example.backend.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService{

    private final VideoRepository videoRep;
    private final ImageServiceImpl imageService;

    @Override
    public Video saveVideo(Video video){
        return videoRep.save(video);
    }

    @Override
    public Video getVideo(Long post_id){
        final Video retrievedVideo = videoRep.getVideoById(post_id);
        if (retrievedVideo != null) {
            return new Video(retrievedVideo.getName(), retrievedVideo.getType(),
                    imageService.decompressBytes(retrievedVideo.getContent()));
        }
        return null;
    }
}
