package com.neuracare.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReportResponse {

    private UUID reportId;
    private UUID userId;
    private String fileName;
    private String status;
    private LocalDateTime uploadTime;

}
