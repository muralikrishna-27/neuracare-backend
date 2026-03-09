package com.neuracare.backend.voice.dto;

import com.neuracare.backend.voice.model.VoiceTask;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VoiceTaskResponse {

    private Long id;

    private String phoneNumber;

    private VoiceTask.TaskType taskType;

    private LocalDateTime scheduledTime;

    private int retryCount;

    private boolean active;
}