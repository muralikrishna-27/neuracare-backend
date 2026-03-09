package com.neuracare.backend.voice.service.impl;

import com.neuracare.backend.voice.dto.VoiceScheduleRequest;
import com.neuracare.backend.voice.dto.VoiceTaskResponse;
import com.neuracare.backend.voice.model.VoiceCallLog;
import com.neuracare.backend.voice.model.VoiceTask;
import com.neuracare.backend.voice.repository.VoiceCallLogRepository;
import com.neuracare.backend.voice.repository.VoiceTaskRepository;
import com.neuracare.backend.voice.service.VoiceCallService;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoiceCallServiceImpl implements VoiceCallService {

    private final VoiceTaskRepository taskRepository;
    private final VoiceCallLogRepository logRepository;

    @Value("${twilio.from.number}")
    private String fromNumber;

    @Value("${app.ngrok.url}")
    private String ngrokUrl;

    @Override
    public VoiceTaskResponse scheduleCall(VoiceScheduleRequest request) {

        VoiceTask task = VoiceTask.builder()
                .userId(request.getUserId())
                .phoneNumber(request.getPhoneNumber())
                .taskType(request.getTaskType())
                .taskMessage(request.getTaskMessage())
                .scheduledTime(request.getScheduledTime())
                .retryCount(0)
                .maxRetries(request.getMaxRetries())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        VoiceTask saved = taskRepository.save(task);

        return VoiceTaskResponse.builder()
                .id(saved.getId())
                .phoneNumber(saved.getPhoneNumber())
                .taskType(saved.getTaskType())
                .scheduledTime(saved.getScheduledTime())
                .retryCount(saved.getRetryCount())
                .active(saved.isActive())
                .build();
    }

    @Override
    public List<VoiceTaskResponse> getTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> VoiceTaskResponse.builder()
                        .id(task.getId())
                        .phoneNumber(task.getPhoneNumber())
                        .taskType(task.getTaskType())
                        .scheduledTime(task.getScheduledTime())
                        .retryCount(task.getRetryCount())
                        .active(task.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String generateIVR() {

        VoiceTask task =
                taskRepository
                        .findTopByActiveTrueOrderByScheduledTimeAsc()
                        .orElse(null);

        String message = "Did you complete today's task?";

        if (task != null && task.getTaskMessage() != null) {
            message = task.getTaskMessage();
        }

        return """
    <Response>
        <Say voice="alice">
            Hello, this is NeuraCare.
            %s
            Press 1 for Yes.
            Press 2 for No.
        </Say>
        <Gather numDigits="1" action="/api/voice/response" method="POST"/>
    </Response>
    """.formatted(message);
    }

    @Override
    public String processResponse(String digits, String callSid) {

        VoiceCallLog.CallResult result =
                "1".equals(digits)
                        ? VoiceCallLog.CallResult.YES
                        : VoiceCallLog.CallResult.NO;

        VoiceTask task = taskRepository
                .findTopByActiveTrueOrderByScheduledTimeAsc()
                .orElse(null);

        logRepository.save(
                VoiceCallLog.builder()
                        .taskId(task != null ? task.getId() : -1L)
                        .callSid(callSid)
                        .callTime(LocalDateTime.now())
                        .result(result)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        if (task != null) {

            if (result == VoiceCallLog.CallResult.NO) {

                task.setRetryCount(task.getRetryCount() + 1);

                if (task.getRetryCount() >= task.getMaxRetries()) {
                    task.setActive(false);
                } else {
                    task.setScheduledTime(LocalDateTime.now().plusMinutes(10));
                }

            } else {
                task.setActive(false);
            }

            taskRepository.save(task);
        }

        return """
        <Response>
            <Say voice="alice">
                Thank you. Take care.
            </Say>
        </Response>
        """;
    }

    @Override
    public void processScheduledCalls() {

        List<VoiceTask> tasks =
                taskRepository.findByActiveTrueAndScheduledTimeBefore(LocalDateTime.now());

        System.out.println("Tasks found: " + tasks.size());

        for (VoiceTask task : tasks) {

            try {

                System.out.println("Calling: " + task.getPhoneNumber());

                Call.creator(
                        new PhoneNumber(task.getPhoneNumber()),
                        new PhoneNumber(fromNumber),
                        URI.create(ngrokUrl + "/api/voice/call")
                ).create();

            } catch (Exception e) {

                e.printStackTrace();

                int retries = task.getRetryCount() + 1;
                task.setRetryCount(retries);

                if (retries >= task.getMaxRetries()) {
                    task.setActive(false);
                } else {
                    task.setScheduledTime(LocalDateTime.now().plusMinutes(10));
                }

                taskRepository.save(task);
            }
        }
    }
}