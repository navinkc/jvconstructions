package com.backend.jvconstructions.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class EnquiryDTOs {

    public record CreateEnquiryRequest(
            String projectCode,
            @NotBlank String name,
            @Email @NotBlank String email,
            @Pattern(regexp = "^[0-9+\\- ]{6,20}$") String phone,
            @Size(max = 2000) String message
    ) {}

    public record EnquiryDTO(
            Long id,
            String projectCode,
            String name,
            String email,
            String phone,
            String message,
            String status,
            String assignedTo,
            Instant createdAt
    ) {}

    public record UpdateEnquiryRequest(
            String status,
            String assignedTo
    ) {}
}
