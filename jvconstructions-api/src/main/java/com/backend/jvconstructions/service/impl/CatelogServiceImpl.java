package com.backend.jvconstructions.service.impl;

import com.backend.jvconstructions.dto.ServiceDTOs;
import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.exception.NotFoundException;
import com.backend.jvconstructions.repository.ServiceRepository;
import com.backend.jvconstructions.service.CatelogService;
import com.backend.jvconstructions.entity.ServiceEntity;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CatelogServiceImpl implements CatelogService {

    private static final Logger log = LoggerFactory.getLogger(EnquiryServiceImpl.class);
    private final ServiceRepository serviceRepository;

    @Override
    public Page<ServiceDTOs.ServiceDTO> getAllServices(Pageable pageable) {
        Page<ServiceEntity> page = serviceRepository.findAll(pageable);
        return page.map(this::toDto);
    }

    @Override
    public ServiceDTOs.ServiceDTO getServiceByName(String name) {
        ServiceEntity service = serviceRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Service with name " + name + " not found."));

        return toDto(service);
    }

    @Override
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ServiceDTOs.ServiceDTO createService(ServiceDTOs.CreateServiceRequest serviceRequest) {
        ServiceEntity service = ServiceEntity.builder()
                .name(serviceRequest.name())
                .description(serviceRequest.description())
                .build();
        service = serviceRepository.save(service);
        return toDto(service);
    }

    @Override
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public ServiceDTOs.ServiceDTO updateService(Long id, ServiceDTOs.UpdateServiceRequest updateServiceRequest) {
        ServiceEntity existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " not found."));
        existingService.setName(updateServiceRequest.name());
        existingService.setDescription(updateServiceRequest.description());
        existingService = serviceRepository.save(existingService);
        return toDto(existingService);
    }

    @Override
    @PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
    public void deleteService(Long id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " not found."));
        serviceRepository.delete(service);
    }

    private ServiceDTOs.ServiceDTO toDto(ServiceEntity service) {
        return new ServiceDTOs.ServiceDTO(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
    }
}
