package com.neuracare.backend.repository;

import com.neuracare.backend.model.ReportChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportChatRepository extends JpaRepository<ReportChatMessage, UUID> {

    List<ReportChatMessage> findTop6ByReportIdOrderByCreatedAtDesc(UUID reportId);

}