package com.backend.jvconstructions.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class ServiceDTOs {

    public record CreateServiceRequest(
            @NotBlank String name,
            String description
    ) {}

    public record ServiceDTO(
            Long id,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt
    ) {}

    public record UpdateServiceRequest(
            String name,
            String description
    ) {}
}
