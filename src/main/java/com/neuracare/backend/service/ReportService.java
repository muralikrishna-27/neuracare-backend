package com.neuracare.backend.service;

import com.neuracare.backend.dto.ReportResponse;
import com.neuracare.backend.dto.UploadReportRequest;
import com.neuracare.backend.model.ReportMetadata;
import com.neuracare.backend.repository.ReportRepository;
import com.neuracare.backend.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportResponse uploadReport(UploadReportRequest request) {

        UUID reportId = IdGenerator.generateReportId();

        ReportMetadata report = new ReportMetadata(
                reportId,
                request.getUserId(),
                request.getFileName(),
                LocalDateTime.now(),
                "UPLOADED"
        );

        reportRepository.save(report);

        return ReportResponse.builder()
                .reportId(reportId)
                .userId(request.getUserId())
                .fileName(request.getFileName())
                .status("UPLOADED")
                .uploadTime(report.getUploadTime())
                .build();
    }

    public ReportResponse getReport(UUID reportId) {

        ReportMetadata report = reportRepository.findById(reportId)
                .orElseThrow();

        return ReportResponse.builder()
                .reportId(report.getReportId())
                .userId(report.getUserId())
                .fileName(report.getFileName())
                .status(report.getStatus())
                .uploadTime(report.getUploadTime())
                .build();
    }

    public List<ReportResponse> getUserReports(UUID userId) {

        return reportRepository.findByUserId(userId)
                .stream()
                .map(r -> ReportResponse.builder()
                        .reportId(r.getReportId())
                        .userId(r.getUserId())
                        .fileName(r.getFileName())
                        .status(r.getStatus())
                        .uploadTime(r.getUploadTime())
                        .build())
                .collect(Collectors.toList());
    }
}