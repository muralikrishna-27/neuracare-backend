package com.neuracare.backend.controller;

import com.neuracare.backend.dto.ReportResponse;
import com.neuracare.backend.dto.UploadReportRequest;
import com.neuracare.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/upload")
    public ReportResponse uploadReport(@RequestBody UploadReportRequest request) {
        return reportService.uploadReport(request);
    }

    @GetMapping("/{reportId}")
    public ReportResponse getReport(@PathVariable UUID reportId) {
        return reportService.getReport(reportId);
    }

    @GetMapping("/user/{userId}")
    public List<ReportResponse> getUserReports(@PathVariable UUID userId) {
        return reportService.getUserReports(userId);
    }
}