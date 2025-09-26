package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
}
