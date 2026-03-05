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
public class ReportMetadata {

    @Id
    @Column(name = "report_id")
    private UUID reportId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "status")
    private String status;
}
