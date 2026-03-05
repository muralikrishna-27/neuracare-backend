package com.neuracare.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UploadReportRequest {

    private UUID userId;
    private String fileName;

}
