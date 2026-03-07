package com.neuracare.backend.service.impl;

import com.neuracare.backend.dto.RuleEngineResponse;
import com.neuracare.backend.model.MedicalObservation;
import com.neuracare.backend.service.MedicalRuleEngineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MedicalRuleEngineServiceImpl implements MedicalRuleEngineService {

    @Override
    public RuleEngineResponse analyze(String text) {

        List<MedicalObservation> observations = new ArrayList<>();

        // ---------------- BLOOD PRESSURE ----------------
        Pattern bpPattern = Pattern.compile("(\\d{2,3})\\s*/\\s*(\\d{2,3})");
        Matcher bpMatch = bpPattern.matcher(text);

        if (bpMatch.find()) {

            int systolic = Integer.parseInt(bpMatch.group(1));
            int diastolic = Integer.parseInt(bpMatch.group(2));

            String risk;

            if (systolic >= 140 || diastolic >= 90) risk = "HIGH";
            else if (systolic >= 130 || diastolic >= 80) risk = "MODERATE";
            else risk = "NORMAL";

            observations.add(new MedicalObservation(
                    "Blood Pressure",
                    systolic,
                    systolic + "/" + diastolic + " mmHg",
                    risk
            ));
        }

        // ---------------- GLUCOSE ----------------
        Pattern glucosePattern = Pattern.compile("glucose[: ]+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher glucoseMatch = glucosePattern.matcher(text);

        if (glucoseMatch.find()) {

            double value = Double.parseDouble(glucoseMatch.group(1));

            String risk;

            if (value >= 126) risk = "HIGH";
            else if (value >= 100) risk = "MODERATE";
            else risk = "NORMAL";

            observations.add(new MedicalObservation(
                    "Glucose",
                    value,
                    "mg/dL",
                    risk
            ));
        }

        // ---------------- CHOLESTEROL ----------------
        Pattern cholPattern = Pattern.compile("cholesterol[: ]+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher cholMatch = cholPattern.matcher(text);

        if (cholMatch.find()) {

            double value = Double.parseDouble(cholMatch.group(1));

            String risk = value >= 240 ? "HIGH" : value >= 200 ? "MODERATE" : "NORMAL";

            observations.add(new MedicalObservation(
                    "Cholesterol",
                    value,
                    "mg/dL",
                    risk
            ));
        }

        // ---------------- HEMOGLOBIN ----------------
        Pattern hemoPattern = Pattern.compile("hemoglobin[: ]+(\\d+\\.?\\d*)", Pattern.CASE_INSENSITIVE);
        Matcher hemoMatch = hemoPattern.matcher(text);

        if (hemoMatch.find()) {

            double value = Double.parseDouble(hemoMatch.group(1));

            String risk = value < 12 ? "HIGH" : "NORMAL";

            observations.add(new MedicalObservation(
                    "Hemoglobin",
                    value,
                    "g/dL",
                    risk
            ));
        }

        // ---------------- PLATELETS ----------------
        Pattern platePattern = Pattern.compile("platelets[: ]+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher plateMatch = platePattern.matcher(text);

        if (plateMatch.find()) {

            double value = Double.parseDouble(plateMatch.group(1));

            String risk = value < 150000 ? "HIGH" : "NORMAL";

            observations.add(new MedicalObservation(
                    "Platelets",
                    value,
                    "/µL",
                    risk
            ));
        }

        // ---------------- WBC ----------------
        Pattern wbcPattern = Pattern.compile("wbc[: ]+(\\d+\\.?\\d*)", Pattern.CASE_INSENSITIVE);
        Matcher wbcMatch = wbcPattern.matcher(text);

        if (wbcMatch.find()) {

            double value = Double.parseDouble(wbcMatch.group(1));

            String risk = value > 11000 ? "HIGH" : "NORMAL";

            observations.add(new MedicalObservation(
                    "WBC",
                    value,
                    "/µL",
                    risk
            ));
        }

        // ---------------- RBC ----------------
        Pattern rbcPattern = Pattern.compile("rbc[: ]+(\\d+\\.?\\d*)", Pattern.CASE_INSENSITIVE);
        Matcher rbcMatch = rbcPattern.matcher(text);

        if (rbcMatch.find()) {

            double value = Double.parseDouble(rbcMatch.group(1));

            String risk = value < 4 ? "HIGH" : "NORMAL";

            observations.add(new MedicalObservation(
                    "RBC",
                    value,
                    "million/µL",
                    risk
            ));
        }

        // ---------------- TRIGLYCERIDES ----------------
        Pattern triPattern = Pattern.compile("triglycerides[: ]+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher triMatch = triPattern.matcher(text);

        if (triMatch.find()) {

            double value = Double.parseDouble(triMatch.group(1));

            String risk = value >= 200 ? "HIGH" : value >= 150 ? "MODERATE" : "NORMAL";

            observations.add(new MedicalObservation(
                    "Triglycerides",
                    value,
                    "mg/dL",
                    risk
            ));
        }

        // ---------------- HDL ----------------
        Pattern hdlPattern = Pattern.compile("hdl[: ]+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher hdlMatch = hdlPattern.matcher(text);

        if (hdlMatch.find()) {

            double value = Double.parseDouble(hdlMatch.group(1));

            String risk = value < 40 ? "HIGH" : "NORMAL";

            observations.add(new MedicalObservation(
                    "HDL",
                    value,
                    "mg/dL",
                    risk
            ));
        }

        // ---------------- LDL ----------------
        Pattern ldlPattern = Pattern.compile("ldl[: ]+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher ldlMatch = ldlPattern.matcher(text);

        if (ldlMatch.find()) {

            double value = Double.parseDouble(ldlMatch.group(1));

            String risk = value >= 160 ? "HIGH" : value >= 130 ? "MODERATE" : "NORMAL";

            observations.add(new MedicalObservation(
                    "LDL",
                    value,
                    "mg/dL",
                    risk
            ));
        }

        // ---------------- CREATININE ----------------
        Pattern creatPattern = Pattern.compile("creatinine[: ]+(\\d+\\.?\\d*)", Pattern.CASE_INSENSITIVE);
        Matcher creatMatch = creatPattern.matcher(text);

        if (creatMatch.find()) {

            double value = Double.parseDouble(creatMatch.group(1));

            String risk = value > 1.3 ? "HIGH" : "NORMAL";

            observations.add(new MedicalObservation(
                    "Creatinine",
                    value,
                    "mg/dL",
                    risk
            ));
        }

        // ---------------- HbA1c ----------------
        Pattern hba1cPattern = Pattern.compile("hba1c[: ]+(\\d+\\.?\\d*)", Pattern.CASE_INSENSITIVE);
        Matcher hba1cMatch = hba1cPattern.matcher(text);

        if (hba1cMatch.find()) {

            double value = Double.parseDouble(hba1cMatch.group(1));

            String risk;

            if (value >= 6.5) risk = "HIGH";
            else if (value >= 5.7) risk = "MODERATE";
            else risk = "NORMAL";

            observations.add(new MedicalObservation(
                    "HbA1c",
                    value,
                    "%",
                    risk
            ));
        }

        String overallRisk = calculateRisk(observations);

        return new RuleEngineResponse(observations, overallRisk);
    }

    private String calculateRisk(List<MedicalObservation> observations) {

        boolean high = observations.stream()
                .anyMatch(o -> o.getRiskLevel().equals("HIGH"));

        boolean moderate = observations.stream()
                .anyMatch(o -> o.getRiskLevel().equals("MODERATE"));

        if (high) return "HIGH";
        if (moderate) return "MODERATE";

        return "LOW";
    }
}