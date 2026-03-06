package com.neuracare.backend.controller;

import com.neuracare.backend.dto.ReportResponse;
import com.neuracare.backend.service.OCRProcessingService;
import com.neuracare.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OCRProcessingService ocrProcessingService;
    private final ReportService reportService;

    // Upload report file
    @PostMapping("/upload")
    public ResponseEntity<ReportResponse> uploadReport(
            @RequestParam("file") MultipartFile file
    ) {
        ReportResponse response = reportService.uploadReport(file);
        return ResponseEntity.ok(response);
    }

    // Get single report metadata
    @GetMapping("/{reportId}")
    public ReportResponse getReport(@PathVariable UUID reportId) {
        return reportService.getReport(reportId);
    }

    // Get all reports for a user
    @GetMapping("/user/{userId}")
    public List<ReportResponse> getUserReports(@PathVariable UUID userId) {
        return reportService.getUserReports(userId);
    }

    @PostMapping("/{reportId}/ocr")
    public String processOCR(@PathVariable UUID reportId) {

        ocrProcessingService.processReport(reportId);

        return "OCR completed";
    }
}