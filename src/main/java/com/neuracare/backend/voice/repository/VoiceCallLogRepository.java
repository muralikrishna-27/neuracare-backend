package com.neuracare.backend.voice.repository;

import com.neuracare.backend.voice.model.VoiceCallLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceCallLogRepository extends JpaRepository<VoiceCallLog, Long> {
}