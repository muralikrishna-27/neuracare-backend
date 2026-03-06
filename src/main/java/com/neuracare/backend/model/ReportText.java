//package com.neuracare.backend.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "report_text")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ReportText {
//
//    @Id
//    private UUID reportId;
//
//    @Column(columnDefinition = "TEXT")
//    private String extractedText;
//
//    private LocalDateTime processedAt;
//}