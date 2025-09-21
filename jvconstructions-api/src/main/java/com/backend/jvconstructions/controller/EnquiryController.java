package com.backend.jvconstructions.controller;

import com.backend.jvconstructions.dto.EnquiryDTOs;
import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.service.EnquiryService;
import jakarta.servlet.http.HttpServletRequest;
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

@RestController
@RequestMapping("/api/v1/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    private static final Logger log = LoggerFactory.getLogger(EnquiryController.class);

    private final EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<EnquiryDTOs.EnquiryDTO> createEnquiry(
            @Valid @RequestBody EnquiryDTOs.CreateEnquiryRequest request,
            HttpServletRequest http) {
        String ip = http.getRemoteAddr();
        String userAgent = http.getHeader("User-Agent");
        return ResponseEntity.ok(enquiryService.create(request, ip, userAgent));
    }

    @GetMapping
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<Page<EnquiryDTOs.EnquiryDTO>> listEnquiries(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(enquiryService.list(status, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<EnquiryDTOs.EnquiryDTO> updateEnquiry(
            @PathVariable Long id,
            @RequestBody EnquiryDTOs.UpdateEnquiryRequest request,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(enquiryService.update(id, request, token.getName()));
    }
}
