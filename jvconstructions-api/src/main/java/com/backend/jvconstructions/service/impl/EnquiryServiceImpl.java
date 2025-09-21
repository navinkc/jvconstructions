package com.backend.jvconstructions.service.impl;

import com.backend.jvconstructions.dto.EnquiryDTOs;
import com.backend.jvconstructions.entity.Enquiry;
import com.backend.jvconstructions.entity.Project;
import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.exception.NotFoundException;
import com.backend.jvconstructions.repository.EnquiryRepository;
import com.backend.jvconstructions.repository.ProjectRepository;
import com.backend.jvconstructions.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnquiryServiceImpl implements EnquiryService {

    private static final Logger log = LoggerFactory.getLogger(EnquiryServiceImpl.class);

    private final EnquiryRepository enquiryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public EnquiryDTOs.EnquiryDTO create(EnquiryDTOs.CreateEnquiryRequest req, String ip, String userAgent) {
        Project p = null;
        if (req.projectCode() != null && !req.projectCode().isBlank()) {
            p = projectRepository.findByCode(req.projectCode())
                    .orElseThrow(() -> new NotFoundException(String.format("Project with code %s not found", req.projectCode())));
        }
        Enquiry enquiry = Enquiry.builder()
                .project(p)
                .name(req.name())
                .email(req.email())
                .phone(req.phone())
                .message(req.message())
                .status("NEW")
                .utmSource(null)
                .build();
        enquiry = enquiryRepository.save(enquiry);
        return toDto(enquiry);
    }

    @Override
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public Page<EnquiryDTOs.EnquiryDTO> list(String status, Pageable pageable) {
        Page<Enquiry> page = (status == null || status.isBlank())
                ? enquiryRepository.findAll(pageable)
                : enquiryRepository.findByStatus(status, pageable);
        return page.map(this::toDto);
    }

    @Override
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public EnquiryDTOs.EnquiryDTO update(Long id, EnquiryDTOs.UpdateEnquiryRequest request, String actor) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Enquiry with id %d not found", id)));
        if (request.status() != null) enquiry.setStatus(request.status());
        if (request.assignedTo() != null) enquiry.setAssignedTo(request.assignedTo());
        enquiry = enquiryRepository.save(enquiry);
        return toDto(enquiry);
    }

    private EnquiryDTOs.EnquiryDTO toDto(Enquiry enquiry) {
        return new EnquiryDTOs.EnquiryDTO(
                enquiry.getId(),
                enquiry.getProject() == null ? null : enquiry.getProject().getCode(),
                enquiry.getName(),
                enquiry.getEmail(),
                enquiry.getPhone(),
                enquiry.getMessage(),
                enquiry.getStatus(),
                enquiry.getAssignedTo(),
                enquiry.getCreatedAt()
        );
    }
}
