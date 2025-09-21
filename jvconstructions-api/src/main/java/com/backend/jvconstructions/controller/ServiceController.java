package com.backend.jvconstructions.controller;

import com.backend.jvconstructions.dto.ServiceDTOs;
import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.service.CatelogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    private static final Logger log = LoggerFactory.getLogger(EnquiryController.class);

    private final CatelogService catelogService;

    @GetMapping
    public ResponseEntity<Page<ServiceDTOs.ServiceDTO>> getAllServices(Pageable pageable) {
        log.info("Received request to get all services with pageable: {}", pageable);
        try {
            Page<ServiceDTOs.ServiceDTO> services = catelogService.getAllServices(pageable);
            log.info("Successfully retrieved {} services", services.getTotalElements());
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error retrieving services", e);
            throw e;
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<ServiceDTOs.ServiceDTO> getServiceByName(@PathVariable String name) {
        log.info("Received request to get service by name: {}", name);
        try {
            ServiceDTOs.ServiceDTO service = catelogService.getServiceByName(name);
            log.info("Successfully retrieved service: {}", service.name());
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Error retrieving service with name: {}", name, e);
            throw e;
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ServiceDTOs.ServiceDTO> createService(
            @Valid @RequestBody ServiceDTOs.CreateServiceRequest request
    ) {
        return ResponseEntity.ok(catelogService.createService(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<ServiceDTOs.ServiceDTO> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceDTOs.UpdateServiceRequest request
    ) {
       return ResponseEntity.ok(catelogService.updateService(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        catelogService.deleteService(id);
        return ResponseEntity.ok("Service with id " + id + " deleted successfully.");
    }
}
