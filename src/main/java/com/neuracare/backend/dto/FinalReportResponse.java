package com.neuracare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FinalReportResponse {

    private String reportId;

    private String summary;

    private String overallRisk;

    private String explanation;

    private List<?> observations;

    private String disclaimer;

}