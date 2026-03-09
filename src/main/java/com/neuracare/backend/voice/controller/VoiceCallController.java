package com.neuracare.backend.voice.controller;

import com.neuracare.backend.voice.dto.VoiceScheduleRequest;
import com.neuracare.backend.voice.dto.VoiceTaskResponse;
import com.neuracare.backend.voice.service.VoiceCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voice")
@RequiredArgsConstructor
public class VoiceCallController {

    private final VoiceCallService voiceCallService;

    @PostMapping("/schedule")
    public VoiceTaskResponse scheduleCall(@RequestBody VoiceScheduleRequest request) {
        return voiceCallService.scheduleCall(request);
    }

    @GetMapping("/tasks")
    public List<VoiceTaskResponse> getTasks() {
        return voiceCallService.getTasks();
    }

    @PostMapping(value = "/call", produces = "application/xml")
    public String playVoice() {
        return voiceCallService.generateIVR();
    }

    @PostMapping("/response")
    public String handleResponse(
            @RequestParam("Digits") String digits,
            @RequestParam("CallSid") String callSid
    ) {
        return voiceCallService.processResponse(digits, callSid);
    }
}