package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.UploadedFile;
import com.cmsfoundation.misportal.repositories.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class FileStorageService {

    @Autowired
    private UploadedFileRepository fileRepository;

    public UploadedFile storeFile(MultipartFile file) throws IOException {
        // Only allow PDF or image files
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") || contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("Only PDF or image files are allowed");
        }

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(file.getOriginalFilename());
        uploadedFile.setFileType(contentType);
        uploadedFile.setFileSize(file.getSize());
        uploadedFile.setData(file.getBytes());
        uploadedFile.setUploadedAt(LocalDateTime.now());

        return fileRepository.save(uploadedFile);
    }

    public UploadedFile getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id " + id));
    }
}
