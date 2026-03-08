package com.neuracare.backend.service;

import com.neuracare.backend.dto.FinalReportResponse;

import java.util.UUID;

public interface FinalReportService {

    FinalReportResponse getFinalReport(UUID reportId);

}