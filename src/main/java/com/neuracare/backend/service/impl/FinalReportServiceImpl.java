package com.neuracare.backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuracare.backend.dto.FinalReportResponse;
import com.neuracare.backend.model.MedicalObservation;
import com.neuracare.backend.model.ReportMetadata;
import com.neuracare.backend.repository.ReportRepository;
import com.neuracare.backend.service.FinalReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinalReportServiceImpl implements FinalReportService {

    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public FinalReportResponse getFinalReport(UUID reportId) {

        ReportMetadata report = reportRepository.findById(reportId)
                .orElseThrow();

        List<MedicalObservation> observations = List.of();

        try {

            if (report.getObservations() != null) {
                observations = objectMapper.readValue(
                        report.getObservations(),
                        new TypeReference<List<MedicalObservation>>() {}
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Failed to parse observations");
        }

        String summary;

        if ("HIGH".equalsIgnoreCase(report.getRiskLevel())) {
            summary = "Some measurements in this report appear above the usual range.";
        } else {
            summary = "The measurements in this report appear within typical ranges.";
        }

        return FinalReportResponse.builder()
                .reportId(report.getReportId().toString())
                .summary(summary)
                .overallRisk(report.getRiskLevel())
                .explanation(report.getAiExplanation())
                .observations(observations)
                .disclaimer("This explanation is not a medical diagnosis. Consult a qualified healthcare professional for medical advice.")
                .build();
    }
}