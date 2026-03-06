package com.neuracare.backend.repository;

import com.neuracare.backend.model.ReportMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportMetadata, UUID> {

    // Get all reports for a user
    List<ReportMetadata> findByUserId(UUID userId);

    // Useful later for OCR / processing queue
    List<ReportMetadata> findByStatus(String status);

    // Get user reports sorted by newest first
    List<ReportMetadata> findByUserIdOrderByUploadTimeDesc(UUID userId);
}