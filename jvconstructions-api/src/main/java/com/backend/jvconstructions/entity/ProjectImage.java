package com.backend.jvconstructions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "project_images", indexes = {
        @Index(name = "idx_project_images_project", columnList = "project_id"),
        @Index(name = "idx_project_images_sort", columnList = "project_id, sortOrder")
})
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ProjectImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "s3_key", nullable = false, columnDefinition = "TEXT")
    private String s3Key;

    @Column(name = "mime_type", nullable = false, length = 64)
    private String mimeType;

    private Integer width;
    private Integer height;
    
    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;
    
    @Builder.Default
    private Boolean hero = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
