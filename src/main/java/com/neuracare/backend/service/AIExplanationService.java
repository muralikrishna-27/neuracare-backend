package com.neuracare.backend.service;

public interface AIExplanationService {

    String generateExplanation(String observationsJson, String riskLevel);

}