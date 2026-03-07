package com.neuracare.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedicalObservation {

    private String parameter;
    private double value;
    private String unit;
    private String riskLevel;

}