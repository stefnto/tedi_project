package com.example.backend.services;

import com.example.backend.models.Image;

public interface ImageService {

    Image saveImage(Image image);

    byte[] compressBytes(byte[] data);

    byte[] decompressBytes(byte[] data);

    Image getImage(Long post_id);
}
