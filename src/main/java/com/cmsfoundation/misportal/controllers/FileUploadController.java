package com.cmsfoundation.misportal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cmsfoundation.misportal.helper.FtpDocumentHelper;
import com.cmsfoundation.misportal.helper.FtpHelper;
import com.cmsfoundation.misportal.helper.FtpVideoHelper;
import com.cmsfoundation.misportal.models.UploadResponse;
import com.cmsfoundation.misportal.services.CompressionService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class FileUploadController {

    @Autowired
    private CompressionService compressionService;

    @Autowired
    private FtpHelper ftpHelper;
    @Autowired
    private FtpVideoHelper ftpVideoHelper;
    @Autowired
    private FtpDocumentHelper ftpDocumentHelper;

    @PostMapping("/upload/images")
    public ResponseEntity<UploadResponse> uploadImages(@RequestParam MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(400, "At least one file is required.", Collections.emptyList()));
        }

        if (files.length > 5) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(400, "Maximum 5 files allowed.", Collections.emptyList()));
        }

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                InputStream compressedInputStream = compressionService.compressImage(inputStream, 1200, 1200, 1.0f);

                String originalFileName = file.getOriginalFilename();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String remoteFileName = originalFileName + "_" + timestamp;

                String fileUrl = ftpHelper.uploadFile(compressedInputStream, remoteFileName);

                if (fileUrl != null) {
                    uploadedUrls.add(fileUrl);
                } else {
                    return ResponseEntity.status(500)
                            .body(new UploadResponse(500, "File upload failed for: " + originalFileName, uploadedUrls));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body(new UploadResponse(500, "Error: " + e.getMessage(), uploadedUrls));
            }
        }

        return ResponseEntity.ok(new UploadResponse(200, "Files uploaded successfully", uploadedUrls));
    }

    // uplaod video
    @PostMapping("/upload/videos")
    public ResponseEntity<UploadResponse> uploadVideos(@RequestParam MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(400, "At least one video file is required.", Collections.emptyList()));
        }

        if (files.length > 5) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(400, "Maximum 5 video files allowed.", Collections.emptyList()));
        }

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                String originalFileName = file.getOriginalFilename();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
               
                String filebaseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));

                filebaseName = filebaseName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
                 String remoteFileName = filebaseName + "_" + timestamp + extension;

                String fileUrl = ftpVideoHelper.uploadFile(inputStream, remoteFileName);

                if (fileUrl != null) {
                    uploadedUrls.add(fileUrl);
                } else {
                    return ResponseEntity.status(500)
                            .body(new UploadResponse(500, "Upload failed for: " + originalFileName, uploadedUrls));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body(new UploadResponse(500, "Error: " + e.getMessage(), uploadedUrls));
            }
        }

        return ResponseEntity.ok(new UploadResponse(200, "Videos uploaded successfully", uploadedUrls));
    }

    // upload documents
    @PostMapping("/upload/documents")
    public ResponseEntity<UploadResponse> uploadDocuments(@RequestParam MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(400, "At least one document  is required.", Collections.emptyList()));
        }

        if (files.length > 5) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(400, "Maximum 5 video files allowed.", Collections.emptyList()));
        }

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                String originalFileName = file.getOriginalFilename();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));

                baseName = baseName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
                String remoteFileName = baseName + "_" + timestamp + extension;
                String fileUrl = ftpDocumentHelper.uploadFile(inputStream, remoteFileName);

                if (fileUrl != null) {
                    uploadedUrls.add(fileUrl);
                } else {
                    return ResponseEntity.status(500)
                            .body(new UploadResponse(500, "Upload failed for: " + originalFileName, uploadedUrls));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body(new UploadResponse(500, "Error: " + e.getMessage(), uploadedUrls));
            }
        }

        return ResponseEntity.ok(new UploadResponse(200, "document uploaded successfully", uploadedUrls));
    }

}
