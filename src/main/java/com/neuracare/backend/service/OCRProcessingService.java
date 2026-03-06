package com.neuracare.backend.service;

import com.neuracare.backend.model.ReportMetadata;
import com.neuracare.backend.ocr.OCRService;
import com.neuracare.backend.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OCRProcessingService {

    private final ReportRepository reportRepository;
    private final OCRService ocrService;

    public void processReport(UUID reportId) {

        ReportMetadata report = reportRepository.findById(reportId)
                .orElseThrow();

        File file = new File(report.getFilePath());

        String extractedText = ocrService.extractText(file);

        report.setExtractedText(extractedText);

        report.setStatus("OCR_COMPLETED");

        reportRepository.save(report);

    }

}