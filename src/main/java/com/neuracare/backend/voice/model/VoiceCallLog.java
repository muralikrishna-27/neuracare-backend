package com.neuracare.backend.voice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "voice_call_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceCallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "call_sid")
    private String callSid;

    @Column(name = "call_time", nullable = false)
    private LocalDateTime callTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private CallResult result;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum CallResult {
        YES,
        NO,
        NO_RESPONSE
    }
}