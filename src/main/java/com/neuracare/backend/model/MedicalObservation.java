package com.neuracare.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalObservation {

    private String parameter;
    private double value;
    private String unit;
    private String riskLevel;

}