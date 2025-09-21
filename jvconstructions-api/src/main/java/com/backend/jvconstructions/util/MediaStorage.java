package com.backend.jvconstructions.util;

import com.backend.jvconstructions.dto.ProjectDTOs;

public interface MediaStorage {
    ProjectDTOs.PresignedUrlResponse createPresignedPut(String key, String mime, long size);
    void deleteObject(String key);
    String toCdnUrl(String key);
    void uploadObject(String key, byte[] data, String contentType);
}
