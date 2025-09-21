package com.backend.jvconstructions.dto;

import com.backend.jvconstructions.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ProjectDTOs {

        public record CreateProjectRequest(
                @NotBlank @Pattern(regexp = "^[a-z0-9-]{3,40}$") String code,
                @NotBlank String name,
                String description,
                @NotNull ProjectStatus projectStatus,
                String city,
                String addressLine1,
                String addressLine2,
                String pinCode,
                LocalDate startDate,
                LocalDate endDate,
                List<String> tags

        ) {
        }

        public record UpdateProjectRequest(
                String name,
                String description,
                ProjectStatus projectStatus,
                String city,
                String addressLine1,
                String addressLine2,
                String pinCode,
                LocalDate startDate,
                LocalDate endDate,
                Long heroImageId
        ) {
        }

        public record ProjectCardDTO(
                Long id,
                String code,
                String name,
                String city,
                ProjectStatus projectStatus,
                String heroImageUrl,
                Instant updatedAt
        ) {
        }

        public record ImageDTO(
                Long id,
                String url,
                String mimeType,
                Integer width,
                Integer height,
                Integer sortOrder,
                boolean hero
        ) {
        }

        public record ProjectDetailDTO(
                Long id,
                String code,
                String name,
                String description,
                String city,
                ProjectStatus projectStatus,
                LocalDate startDate,
                LocalDate endDate,
                List<ImageDTO> images
        ) {
        }

        public record PresignedUrlRequest(
                @NotBlank String mimeType,
                @Positive long sizeBytes
        ) {
        }

        public record PresignedUrlResponse(
                String uploadUrl,
                String method,
                Map<String, String> headers,
                int expiresIn,
                String s3Key
        ) {
        }

        public record ConfirmImageReq(
                @NotBlank String s3Key,
                @NotBlank String mimeType,
                Integer width,
                Integer height,
                @Positive long sizeBytes,
                Boolean isHero,
                Integer sortOrder
        ) {
        }
}
