package com.neuracare.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuracare.backend.dto.ReportChatRequest;
import com.neuracare.backend.dto.ReportChatResponse;
import com.neuracare.backend.model.ReportChatMessage;
import com.neuracare.backend.model.ReportMetadata;
import com.neuracare.backend.repository.ReportChatRepository;
import com.neuracare.backend.repository.ReportRepository;
import com.neuracare.backend.service.ReportChatService;
import com.neuracare.backend.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportChatServiceImpl implements ReportChatService {

    private final ReportRepository reportRepository;
    private final ReportChatRepository chatRepository;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    @Value("${groq.temperature}")
    private double temperature;

    @Override
    public ReportChatResponse chat(UUID reportId, ReportChatRequest request) {

        ReportMetadata report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        List<ReportChatMessage> history =
                chatRepository.findTop6ByReportIdOrderByCreatedAtDesc(reportId);

        String prompt = buildPrompt(report, history, request.getQuestion());

        String aiAnswer = callGroq(prompt);

        saveMessage(reportId, "USER", request.getQuestion());
        saveMessage(reportId, "AI", aiAnswer);

        return ReportChatResponse.builder()
                .answer(aiAnswer)
                .disclaimer("This explanation is not medical advice. Please consult a healthcare professional.")
                .build();
    }

    private String buildPrompt(ReportMetadata report,
                               List<ReportChatMessage> history,
                               String question) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
You are a medical report explanation assistant.

Rules:
- Answer ONLY using the report information.
- Do NOT provide diagnosis or treatment.
- Do NOT invent medical values.
- If the question is unrelated to the report, politely refuse.

Report Text:
""");

        prompt.append(report.getExtractedText()).append("\n\n");

        prompt.append("Observations:\n");
        prompt.append(report.getObservations()).append("\n\n");

        prompt.append("Previous Conversation:\n");

        for (ReportChatMessage msg : history) {
            prompt.append(msg.getRole()).append(": ")
                    .append(msg.getMessage()).append("\n");
        }

        prompt.append("\nUser Question:\n");
        prompt.append(question);

        return prompt.toString();
    }

    private String callGroq(String prompt) {

        try {

            var client = java.net.http.HttpClient.newHttpClient();

            var requestBody = java.util.Map.of(
                    "model", model,
                    "temperature", temperature,
                    "messages", java.util.List.of(
                            java.util.Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    )
            );

            String body = objectMapper.writeValueAsString(requestBody);

            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(new java.net.URI(apiUrl))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                    .build();

            var response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());
            System.out.println("Groq Response: " + response.body());
            var json = objectMapper.readTree(response.body());

            return json
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I couldn't generate an explanation for this question.";

        }

    }

    private void saveMessage(UUID reportId, String role, String message) {

        chatRepository.save(
                ReportChatMessage.builder()
                        .messageId(UUID.randomUUID())
                        .reportId(reportId)
                        .role(role)
                        .message(message)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

}