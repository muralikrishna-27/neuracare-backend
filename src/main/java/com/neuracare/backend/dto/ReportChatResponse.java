package com.neuracare.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportChatResponse {

    private String answer;

    private String disclaimer;

}