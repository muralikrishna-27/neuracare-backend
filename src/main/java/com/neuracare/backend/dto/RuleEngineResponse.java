package com.neuracare.backend.dto;

import com.neuracare.backend.model.MedicalObservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RuleEngineResponse {

    private List<MedicalObservation> observations;
    private String overallRisk;

}