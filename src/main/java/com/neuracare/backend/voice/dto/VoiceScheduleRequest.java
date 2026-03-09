package com.neuracare.backend.voice.dto;

import com.neuracare.backend.voice.model.VoiceTask;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoiceScheduleRequest {

    private Long userId;

    private String phoneNumber;

    private VoiceTask.TaskType taskType;

    private LocalDateTime scheduledTime;

    private Integer maxRetries;

    private String taskMessage;
}