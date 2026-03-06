package com.neuracare.backend.service;

import com.neuracare.backend.dto.ReportResponse;
import com.neuracare.backend.model.ReportMetadata;
import com.neuracare.backend.repository.ReportRepository;
import com.neuracare.backend.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportResponse uploadReport(MultipartFile file) {

        try {

            UUID reportId = IdGenerator.generateReportId();

            String fileName = file.getOriginalFilename();

            // absolute uploads directory
            String uploadDir = System.getProperty("user.dir") + "/uploads";

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = uploadDir + "/" + reportId + "_" + fileName;

            File savedFile = new File(filePath);

            file.transferTo(savedFile);

            ReportMetadata report = ReportMetadata.builder()
                    .reportId(reportId)
                    .userId(null)
                    .fileName(fileName)
                    .filePath(filePath)
                    .uploadTime(LocalDateTime.now())
                    .status("UPLOADED")
                    .build();

            reportRepository.save(report);

            return ReportResponse.builder()
                    .reportId(reportId)
                    .userId(null)
                    .fileName(fileName)
                    .filePath(filePath)
                    .status("UPLOADED")
                    .uploadTime(report.getUploadTime())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    public ReportResponse getReport(UUID reportId) {

        ReportMetadata report = reportRepository.findById(reportId)
                .orElseThrow();

        return ReportResponse.builder()
                .reportId(report.getReportId())
                .userId(report.getUserId())
                .fileName(report.getFileName())
                .filePath(report.getFilePath())
                .status(report.getStatus())
                .uploadTime(report.getUploadTime())
                .build();
    }

    public List<ReportResponse> getUserReports(UUID userId) {

        return reportRepository.findByUserIdOrderByUploadTimeDesc(userId)
                .stream()
                .map(r -> ReportResponse.builder()
                        .reportId(r.getReportId())
                        .userId(r.getUserId())
                        .fileName(r.getFileName())
                        .filePath(r.getFilePath())
                        .status(r.getStatus())
                        .uploadTime(r.getUploadTime())
                        .build())
                .collect(Collectors.toList());
    }
}