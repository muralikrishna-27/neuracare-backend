package com.neuracare.backend.service.impl;

import com.neuracare.backend.service.SafetyFilterService;
import org.springframework.stereotype.Service;

@Service
public class SafetyFilterServiceImpl implements SafetyFilterService {

    @Override
    public String filter(String aiResponse) {

        if (aiResponse == null) {
            return "";
        }

        String filtered = aiResponse;

        // remove panic words
        filtered = filtered.replaceAll("(?i)dangerous", "above the normal range");
        filtered = filtered.replaceAll("(?i)critical", "higher than typical levels");
        filtered = filtered.replaceAll("(?i)severe", "not within the usual range");
        filtered = filtered.replaceAll("(?i)fatal", "serious");

        // normalize tone
        filtered = filtered.replaceAll("(?i)you must", "it may be helpful to");
        filtered = filtered.replaceAll("(?i)immediately", "soon");

        // ensure disclaimer exists
        if (!filtered.toLowerCase().contains("not a medical diagnosis")) {

            filtered += "\n\nNote: This explanation only describes the values "
                    + "found in the report and is not a medical diagnosis. "
                    + "For personalized medical guidance, please consult a "
                    + "qualified healthcare professional.";
        }

        return filtered;
    }
}