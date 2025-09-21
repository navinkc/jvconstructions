package com.backend.jvconstructions.service.impl;

import com.backend.jvconstructions.dto.ProjectDTOs;
import com.backend.jvconstructions.entity.Project;
import com.backend.jvconstructions.entity.ProjectImage;
import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.enums.ProjectStatus;
import com.backend.jvconstructions.exception.BadRequestException;
import com.backend.jvconstructions.exception.NotFoundException;
import com.backend.jvconstructions.repository.ProjectImageRepository;
import com.backend.jvconstructions.repository.ProjectRepository;
import com.backend.jvconstructions.service.ProjectService;
import com.backend.jvconstructions.util.DtoMapper;
import com.backend.jvconstructions.util.MediaStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MediaStorage mediaStorage;

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ProjectDTOs.ProjectDetailDTO create(ProjectDTOs.CreateProjectRequest req, String actor) {
        if (projectRepository.existsByCode(req.code())) throw new BadRequestException("Project code already exists");
        Project p = Project.builder()
                .code(req.code())
                .name(req.name())
                .description(req.description())
                .projectStatus(req.projectStatus())
                .city(req.city())
                .addressLine1(req.addressLine1())
                .addressLine2(req.addressLine2())
                .pinCode(req.pinCode())
                .startDate(req.startDate())
                .endDate(req.endDate())
                .build();
        p = projectRepository.save(p);
        return DtoMapper.toDetail(p, mediaStorage::toCdnUrl);
    }

    @Override
    public Page<ProjectDTOs.ProjectCardDTO> list(ProjectStatus status, String city, Pageable pageable) {
        Page<Project> page = (status != null)
                ? projectRepository.findByProjectStatus(status, pageable)
                : projectRepository.findAll(pageable);
        return page.map(p -> DtoMapper.toCard(p, mediaStorage::toCdnUrl));
    }

    @Override
    public ProjectDTOs.ProjectDetailDTO getByCode(String code) {
        Project p = projectRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Project with code not found."));
        return DtoMapper.toDetail(p, mediaStorage::toCdnUrl);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ProjectDTOs.ProjectDetailDTO update(Long id, ProjectDTOs.UpdateProjectRequest req, String actor) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", id)));
        if (req.name() != null) p.setName(req.name());
        if (req.description() != null) p.setDescription(req.description());
        if (req.projectStatus() != null) p.setProjectStatus(req.projectStatus());
        if (req.city() != null) p.setCity(req.city());
        if (req.addressLine1() != null) p.setAddressLine1(req.addressLine1());
        if (req.addressLine2() != null) p.setAddressLine2(req.addressLine2());
        if (req.pinCode() != null) p.setPinCode(req.pinCode());
        if (req.startDate() != null) p.setStartDate(req.startDate());
        if (req.endDate() != null) p.setEndDate(req.endDate());
        if (req.heroImageId() != null) p.setHeroImageId(req.heroImageId());
        return DtoMapper.toDetail(p, mediaStorage::toCdnUrl);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public void delete(Long id, String actor) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", id)));
        for (ProjectImage projectImage : p.getImages()) {
            mediaStorage.deleteObject(projectImage.getS3Key());
        }
        projectRepository.delete(p);
    }

    @Override
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ProjectDTOs.PresignedUrlResponse getPresignedUploadUrl(Long projectId, ProjectDTOs.PresignedUrlRequest req, String actor) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", projectId)));
        if (!req.mimeType().startsWith("image/")) throw new BadRequestException("Only images allowed.");
        String ext = switch (req.mimeType()) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new BadRequestException("Unsupported image type.");
        };
        String key = String.format("projects/%s/images/%s.%s", p.getCode(), UUID.randomUUID(), ext);
        return mediaStorage.createPresignedPut(key, req.mimeType(), req.sizeBytes());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ProjectDTOs.ImageDTO confirmImage(Long projectId, ProjectDTOs.ConfirmImageReq req, String actor) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", projectId)));
        if (req.s3Key() == null || !req.s3Key().startsWith("projects/" + p.getCode()))
            throw new BadRequestException(String.format("s3Key %s does not belong to this project", req.s3Key()));
        ProjectImage image = ProjectImage.builder()
                .project(p)
                .s3Key(req.s3Key())
                .mimeType(req.mimeType())
                .width(req.width())
                .height(req.height())
                .sizeBytes(req.sizeBytes())
                .sortOrder(req.sortOrder() == null ? 0 : req.sortOrder())
                .hero(Boolean.TRUE.equals(req.isHero()))
                .build();
        image = projectImageRepository.save(image);

        if (Boolean.TRUE.equals(req.isHero())) p.setHeroImageId(image.getId());
        return new ProjectDTOs.ImageDTO(
                image.getId(),
                mediaStorage.toCdnUrl(image.getS3Key()),
                image.getMimeType(),
                image.getWidth(),
                image.getHeight(),
                image.getSortOrder(),
                Objects.equals(p.getHeroImageId(), image.getId())
        );
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public void deleteImage(Long projectId, Long imageId, String actor) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", projectId)));
        ProjectImage image = projectImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException(String.format("Image with id %d not found.", imageId)));
        if (!image.getProject().getId().equals(p.getId())) throw new BadRequestException("Image not in project.");
        mediaStorage.deleteObject(image.getS3Key());
        projectImageRepository.delete(image);
        if (Objects.equals(p.getHeroImageId(), imageId)) p.setHeroImageId(null);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public void setHeroImage(Long projectId, Long imageId, String actor) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", projectId)));
        ProjectImage image = projectImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException(String.format("Image with id %d not found.", imageId)));
        if (!image.getProject().getId().equals(p.getId())) throw new BadRequestException("Image not in project.");
        p.setHeroImageId(imageId);
        image.setHero(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ProjectDTOs.ImageDTO uploadImage(Long projectId, MultipartFile file, Boolean isHero, Integer sortOrder, String actor) {
        try {
            // Validate project exists
            Project p = projectRepository.findById(projectId)
                    .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", projectId)));

            // Validate file
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)$")) {
                throw new BadRequestException("Only image files (jpg, jpeg, png, webp) are allowed");
            }

            // Generate S3 key
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String s3Key = String.format("projects/%s/images/%s%s", p.getCode(), UUID.randomUUID(), extension);

            // Upload to S3 directly
            mediaStorage.uploadObject(s3Key, file.getBytes(), file.getContentType());

            // Create database record
            ProjectImage image = ProjectImage.builder()
                    .project(p)
                    .s3Key(s3Key)
                    .mimeType(file.getContentType())
                    .sizeBytes(file.getSize())
                    .sortOrder(sortOrder != null ? sortOrder : 0)
                    .hero(Boolean.TRUE.equals(isHero))
                    .build();

            image = projectImageRepository.save(image);

            // Set as hero image if requested
            if (Boolean.TRUE.equals(isHero)) {
                p.setHeroImageId(image.getId());
                projectRepository.save(p);
            }

            log.info("Successfully uploaded image {} for project {}", s3Key, projectId);

            return new ProjectDTOs.ImageDTO(
                    image.getId(),
                    mediaStorage.toCdnUrl(image.getS3Key()),
                    image.getMimeType(),
                    null, // width - would need image processing to get this
                    null, // height - would need image processing to get this
                    image.getSortOrder(),
                    Objects.equals(p.getHeroImageId(), image.getId())
            );

        } catch (IOException e) {
            log.error("Error reading file for project {}", projectId, e);
            throw new BadRequestException("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error uploading image for project {}", projectId, e);
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public List<ProjectDTOs.ImageDTO> uploadMultipleImages(Long projectId, MultipartFile[] files, Boolean isHero, String actor) {
        try {
            // Validate project exists
            Project p = projectRepository.findById(projectId)
                    .orElseThrow(() -> new NotFoundException(String.format("Project with id %d not found.", projectId)));

            // Validate files
            if (files == null || files.length == 0) {
                throw new BadRequestException("No files provided");
            }

            if (files.length > 10) {
                throw new BadRequestException("Maximum 10 files allowed per upload");
            }

            List<ProjectDTOs.ImageDTO> uploadedImages = new ArrayList<>();
            List<ProjectImage> savedImages = new ArrayList<>();

            // Process each file
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                
                // Validate individual file
                if (file.isEmpty()) {
                    log.warn("Skipping empty file at index {}", i);
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || !originalFilename.toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)$")) {
                    log.warn("Skipping invalid file type: {}", originalFilename);
                    continue;
                }

                // Generate S3 key
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String s3Key = String.format("projects/%s/images/%s%s", p.getCode(), UUID.randomUUID(), extension);

                // Upload to S3 directly
                mediaStorage.uploadObject(s3Key, file.getBytes(), file.getContentType());

                // Create database record
                ProjectImage image = ProjectImage.builder()
                        .project(p)
                        .s3Key(s3Key)
                        .mimeType(file.getContentType())
                        .sizeBytes(file.getSize())
                        .sortOrder(i) // Use index as sort order
                        .hero(false) // Only first image can be hero if isHero is true
                        .build();

                image = projectImageRepository.save(image);
                savedImages.add(image);

                log.info("Successfully uploaded image {} for project {} (index {})", s3Key, projectId, i);
            }

            // Set first image as hero if requested
            if (Boolean.TRUE.equals(isHero) && !savedImages.isEmpty()) {
                ProjectImage firstImage = savedImages.get(0);
                firstImage.setHero(true);
                projectImageRepository.save(firstImage);
                p.setHeroImageId(firstImage.getId());
                projectRepository.save(p);
            }

            // Build response DTOs
            for (ProjectImage image : savedImages) {
                uploadedImages.add(new ProjectDTOs.ImageDTO(
                        image.getId(),
                        mediaStorage.toCdnUrl(image.getS3Key()),
                        image.getMimeType(),
                        null, // width - would need image processing to get this
                        null, // height - would need image processing to get this
                        image.getSortOrder(),
                        Objects.equals(p.getHeroImageId(), image.getId())
                ));
            }

            log.info("Successfully uploaded {} images for project {}", uploadedImages.size(), projectId);
            return uploadedImages;

        } catch (IOException e) {
            log.error("Error reading files for project {}", projectId, e);
            throw new BadRequestException("Error reading files: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error uploading multiple images for project {}", projectId, e);
            throw new RuntimeException("Failed to upload images: " + e.getMessage(), e);
        }
    }
}
