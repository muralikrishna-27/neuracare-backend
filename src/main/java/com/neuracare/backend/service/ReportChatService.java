package com.neuracare.backend.service;

import com.neuracare.backend.dto.ReportChatRequest;
import com.neuracare.backend.dto.ReportChatResponse;

import java.util.UUID;

public interface ReportChatService {

    ReportChatResponse chat(UUID reportId, ReportChatRequest request);

}