package com.cmsfoundation.misportal.services;

import java.io.InputStream;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class CompressionServiceImpl implements CompressionService {
@Override
	public InputStream compressImage(InputStream inputStream, int width, int height, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .size(width, height)  // Reduce image resolution
                .outputQuality(quality) // Set compression quality
                .toOutputStream(outputStream);
        
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
