package com.example.backend.services;

import com.example.backend.models.Audio;

public interface AudioService {
    Audio saveAudio(Audio audio);

    Audio getAudio(Long post_id);
}
