package com.example.backend.services;

import com.example.backend.models.Image;
import com.example.backend.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService{

   private final ImageRepository imageRepository;

    @Override
    public Image saveImage(Image image){
        return imageRepository.save(image);
    }

    @Override
    public Image getImage(Long post_id){
        final Image retrievedImage = imageRepository.getImageById(post_id);
        if (retrievedImage != null) {
            return new Image(retrievedImage.getName(), retrievedImage.getType(),
                    decompressBytes(retrievedImage.getContent()));
        }
        return null;
    }

    // compress the image bytes before storing it in the database
    @Override
    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    @Override
    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }
}
