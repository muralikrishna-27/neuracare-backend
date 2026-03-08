package com.neuracare.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuracare.backend.service.AIExplanationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIExplanationServiceImpl implements AIExplanationService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    @Override
    public String generateExplanation(String observationsJson, String riskLevel) {

        try {

            String prompt = """
You are a medical report explanation assistant.

The following JSON contains medical observations extracted from a lab report.

Each object contains:
parameter → name of the test
value → measured value
unit → measurement unit
riskLevel → NORMAL or HIGH

Explain each parameter in simple, calm language suitable for elderly users.
Explain each medical parameter separately.
Create a section for every parameter found in the observations.
Rules:
- Explain each parameter separately
- Mention whether the value is normal or elevated
- Do NOT diagnose diseases
- Do NOT recommend treatment
- Use short bullet points
- End with a neutral disclaimer that medical guidance should come from a healthcare professional

Observations:
""" + observationsJson + """

Overall Risk Level:
""" + riskLevel;

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", new Object[]{
                            Map.of("role", "user", "content", prompt)
                    }
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(apiUrl, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());

            return root
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {

            return "Explanation could not be generated.";

        }

    }
}