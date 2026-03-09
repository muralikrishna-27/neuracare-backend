package com.neuracare.backend.voice.repository;

import com.neuracare.backend.voice.model.VoiceTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoiceTaskRepository extends JpaRepository<VoiceTask, Long> {

    List<VoiceTask> findByActiveTrueAndScheduledTimeBefore(LocalDateTime time);

    Optional<VoiceTask> findTopByActiveTrueOrderByScheduledTimeAsc();
}