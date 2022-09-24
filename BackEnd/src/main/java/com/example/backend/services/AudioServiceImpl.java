package com.example.backend.services;

import com.example.backend.models.Audio;
import com.example.backend.repositories.AudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRep;
    private final ImageServiceImpl imageService;

    @Override
    public Audio saveAudio(Audio audio){return audioRep.save(audio);}

    @Override
    public Audio getAudio(Long post_id){
        final Audio retrievedAudio = audioRep.getAudioById(post_id);
        if (retrievedAudio != null) {
            return new Audio(retrievedAudio.getName(), retrievedAudio.getType(),
                    imageService.decompressBytes(retrievedAudio.getContent()));
        }
        return null;
    }
}
