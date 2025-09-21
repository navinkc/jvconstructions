package com.backend.jvconstructions.controller;

import com.backend.jvconstructions.dto.ProjectDTOs;
import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.enums.ProjectStatus;
import com.backend.jvconstructions.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectDTOs.ProjectCardDTO>> list(
            @RequestParam(required = false) ProjectStatus projectStatus,
            @RequestParam(required = false) String city,
            Pageable pageable
            ) {
        log.info("Received request to get projects with status: {}, city: {}, pageable: {}", projectStatus, city, pageable);
        try {
            Page<ProjectDTOs.ProjectCardDTO> projects = projectService.list(projectStatus, city, pageable);
            log.info("Successfully retrieved {} projects", projects.getTotalElements());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            log.error("Error retrieving projects", e);
            throw e;
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProjectDTOs.ProjectDetailDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(projectService.getByCode(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ProjectDTOs.ProjectDetailDTO> createProject(
            @Valid @RequestBody ProjectDTOs.CreateProjectRequest request,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(projectService.create(request, token.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ProjectDTOs.ProjectDetailDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDTOs.UpdateProjectRequest request,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(projectService.update(id, request, token.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<String> deleteProject(@PathVariable Long id, JwtAuthenticationToken token) {
        projectService.delete(id, token.getName());
        return ResponseEntity.ok("Project with id " + id + " deleted successfully.");
    }

    @PostMapping("/{id}/images/upload-url")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ProjectDTOs.PresignedUrlResponse> uploadUrl(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDTOs.PresignedUrlRequest request,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(projectService.getPresignedUploadUrl(id, request, token.getName()));
    }

    @PostMapping("/{id}/images/confirm")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ProjectDTOs.ImageDTO> confirmImage(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDTOs.ConfirmImageReq request,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(projectService.confirmImage(id, request, token.getName()));
    }

    @DeleteMapping("/{projectId}/images/{imageId}")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<String> deleteImage(@PathVariable Long projectId, @PathVariable Long imageId, JwtAuthenticationToken token) {
        projectService.deleteImage(projectId, imageId, token.getName());
        return ResponseEntity.ok("Image with id " + imageId + " deleted successfully.");
    }

    @PutMapping("/{projectId}/images/{imageId}/hero")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<String> setHero(@PathVariable Long projectId, @PathVariable Long imageId, JwtAuthenticationToken token) {
        projectService.setHeroImage(projectId, imageId, token.getName());
        return ResponseEntity.ok("Hero image set successfully for is " + imageId);
    }

    @PostMapping("/{id}/images/upload")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ProjectDTOs.ImageDTO> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isHero", defaultValue = "false") Boolean isHero,
            @RequestParam(value = "sortOrder", defaultValue = "0") Integer sortOrder,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(projectService.uploadImage(id, file, isHero, sortOrder, token.getName()));
    }

    @PostMapping("/{id}/images/upload-multiple")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<List<ProjectDTOs.ImageDTO>> uploadMultipleImages(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "isHero", defaultValue = "false") Boolean isHero,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(projectService.uploadMultipleImages(id, files, isHero, token.getName()));
    }
}
