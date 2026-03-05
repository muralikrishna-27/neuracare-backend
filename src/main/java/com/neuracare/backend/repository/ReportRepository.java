package com.neuracare.backend.repository;

import com.neuracare.backend.model.ReportMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportMetadata, UUID> {

    List<ReportMetadata> findByUserId(UUID userId);

}
