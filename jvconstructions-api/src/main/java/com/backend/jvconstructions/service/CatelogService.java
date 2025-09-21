package com.backend.jvconstructions.service;

import com.backend.jvconstructions.dto.ServiceDTOs;
import com.backend.jvconstructions.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatelogService {
    public Page<ServiceDTOs.ServiceDTO> getAllServices(Pageable pageable);
    public ServiceDTOs.ServiceDTO getServiceByName(String name);
    public ServiceDTOs.ServiceDTO createService(ServiceDTOs.CreateServiceRequest serviceRequest);
    public ServiceDTOs.ServiceDTO updateService(Long id, ServiceDTOs.UpdateServiceRequest updateServiceRequest);
    public void deleteService(Long id);
}
