package com.neuracare.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "report_chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportChatMessage {

    @Id
    @Column(name = "message_id")
    private UUID messageId;

    @Column(name = "report_id")
    private UUID reportId;

    @Column(name = "role")
    private String role;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}