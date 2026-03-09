package com.neuracare.backend.voice.service;

import com.neuracare.backend.voice.dto.VoiceScheduleRequest;
import com.neuracare.backend.voice.dto.VoiceTaskResponse;

import java.util.List;

public interface VoiceCallService {

    VoiceTaskResponse scheduleCall(VoiceScheduleRequest request);

    List<VoiceTaskResponse> getTasks();

    String generateIVR();

    String processResponse(String digits, String callSid);

    void processScheduledCalls();
}