package com.backend.jvconstructions.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "enquiries", indexes = {
        @Index(name = "idx_enquiries_project", columnList = "project_id"),
        @Index(name = "idx_enquiries_status", columnList = "status"),
        @Index(name = "idx_enquiries_created_at", columnList = "createdAt")
})
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Enquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(length = 20)
    private String phone;

    @Lob
    private String message;

    @Column(nullable = false, length = 24)
    @Builder.Default
    private String status = "NEW";

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private String assignedTo;
    private String utmSource;
}
