package com.neuracare.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuracare.backend.dto.RuleEngineResponse;
import com.neuracare.backend.model.ReportMetadata;
import com.neuracare.backend.ocr.OCRService;
import com.neuracare.backend.repository.ReportRepository;
import com.neuracare.backend.service.MedicalRuleEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OCRProcessingService {

    private final ReportRepository reportRepository;
    private final OCRService ocrService;
    private final MedicalRuleEngineService medicalRuleEngineService;
    private final ObjectMapper objectMapper;   // ✅ ADD THIS

    public void processReport(UUID reportId) {

        try {

            ReportMetadata report = reportRepository.findById(reportId)
                    .orElseThrow();

            File file = new File(report.getFilePath());

            String extractedText = ocrService.extractText(file);

            RuleEngineResponse ruleResult =
                    medicalRuleEngineService.analyze(extractedText);

            report.setExtractedText(extractedText);
            report.setRiskLevel(ruleResult.getOverallRisk());
            report.setStatus("OCR_COMPLETED");

            // store observations JSON
            report.setObservations(
                    objectMapper.writeValueAsString(ruleResult.getObservations())
            );

            reportRepository.save(report);

        } catch (Exception e) {

            throw new RuntimeException("OCR processing failed", e);

        }
    }
}