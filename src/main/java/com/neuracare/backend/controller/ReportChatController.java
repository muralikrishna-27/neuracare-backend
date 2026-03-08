package com.neuracare.backend.controller;

import com.neuracare.backend.dto.ReportChatRequest;
import com.neuracare.backend.dto.ReportChatResponse;
import com.neuracare.backend.service.ReportChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportChatController {

    private final ReportChatService reportChatService;

    @PostMapping("/{reportId}/chat")
    public ReportChatResponse chat(
            @PathVariable UUID reportId,
            @RequestBody ReportChatRequest request
    ) {

        return reportChatService.chat(reportId, request);

    }
}