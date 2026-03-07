package com.neuracare.backend.service;

import com.neuracare.backend.dto.RuleEngineResponse;

public interface MedicalRuleEngineService {

    RuleEngineResponse analyze(String extractedText);

}