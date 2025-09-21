package com.backend.jvconstructions.service;

import com.backend.jvconstructions.dto.ProjectDTOs;
import com.backend.jvconstructions.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    ProjectDTOs.ProjectDetailDTO create(ProjectDTOs.CreateProjectRequest req, String actor);
    Page<ProjectDTOs.ProjectCardDTO> list(ProjectStatus status, String city, Pageable pageable);
    ProjectDTOs.ProjectDetailDTO getByCode(String code);
    ProjectDTOs.ProjectDetailDTO update(Long id, ProjectDTOs.UpdateProjectRequest req, String actor);
    void delete(Long id, String actor);
    ProjectDTOs.PresignedUrlResponse getPresignedUploadUrl(Long projectId, ProjectDTOs.PresignedUrlRequest req, String actor);
    ProjectDTOs.ImageDTO confirmImage(Long projectId, ProjectDTOs.ConfirmImageReq req, String actor);
    void deleteImage(Long projectId, Long imageId, String actor);
    void setHeroImage(Long projectId, Long imageId, String actor);
    ProjectDTOs.ImageDTO uploadImage(Long projectId, MultipartFile file, Boolean isHero, Integer sortOrder, String actor);
    List<ProjectDTOs.ImageDTO> uploadMultipleImages(Long projectId, MultipartFile[] files, Boolean isHero, String actor);
}
