package com.backend.jvconstructions.service;

import com.backend.jvconstructions.dto.EnquiryDTOs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnquiryService {
    EnquiryDTOs.EnquiryDTO create(EnquiryDTOs.CreateEnquiryRequest req, String ip, String userAgent);
    Page<EnquiryDTOs.EnquiryDTO> list(String status, Pageable pageable);
    EnquiryDTOs.EnquiryDTO update(Long id, EnquiryDTOs.UpdateEnquiryRequest request, String actor);
}
