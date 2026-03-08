package com.neuracare.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMetadata {

    @Id
    @Column(name = "report_id")
    private UUID reportId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "file_name")
    private String fileName;

    // NEW FIELD (important for OCR)
    @Column(name = "file_path")
    private String filePath;

    @Column(name = "extracted_text", columnDefinition = "TEXT")
    private String extractedText;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "observations", columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private String observations;

    @Column(name = "ai_explanation", columnDefinition = "TEXT")
    private String aiExplanation;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "status")
    private String status;
}