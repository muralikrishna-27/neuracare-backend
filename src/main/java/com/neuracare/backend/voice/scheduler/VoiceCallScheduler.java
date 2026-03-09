package com.neuracare.backend.voice.scheduler;

import com.neuracare.backend.voice.service.VoiceCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoiceCallScheduler {

    private final VoiceCallService voiceCallService;

    @Scheduled(fixedRate = 60000)
    public void processVoiceTasks() {
        System.out.println("Scheduler triggered");

        voiceCallService.processScheduledCalls();
    }
}