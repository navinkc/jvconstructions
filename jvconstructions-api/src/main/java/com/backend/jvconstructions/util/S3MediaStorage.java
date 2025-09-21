package com.backend.jvconstructions.util;

import com.backend.jvconstructions.dto.ProjectDTOs;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class S3MediaStorage implements MediaStorage {

    private static final Logger log = LoggerFactory.getLogger(S3MediaStorage.class);

    private final S3Presigner presigner;
    private final S3Client s3;

    @Value("${media.s3.bucket}") String bucket;
    @Value("${media.s3.cdnDomain}") String cdnDomain;
    @Value("${media.s3.presignExpirySeconds:300}") int expiry;

    @Override
    public ProjectDTOs.PresignedUrlResponse createPresignedPut(String key, String mime, long size) {
        try {
            log.info("Creating presigned URL for key: {}, mime: {}, size: {}", key, mime, size);
            
            // Create PutObjectRequest with ACL properly included in signature
            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(mime)
                    .contentLength(size)
                    .acl(ObjectCannedACL.PRIVATE)
                    .build();
            
            // Generate presigned URL
            PresignedPutObjectRequest pre = presigner.presignPutObject(b -> b
                    .putObjectRequest(put)
                    .signatureDuration(Duration.ofSeconds(expiry))
            );
            
            String presignedUrl = pre.url().toString();
            log.info("Generated presigned URL: {}", presignedUrl);
            
            // Return headers including ACL
            return new ProjectDTOs.PresignedUrlResponse(
                    presignedUrl,
                    "PUT",
                    Map.of(
                        "Content-Type", mime,
                        "x-amz-acl", "private"
                    ),
                    expiry,
                    key
            );
        } catch (Exception e) {
            log.error("Error creating presigned URL for key: {}", key, e);
            throw new RuntimeException("Failed to create presigned URL: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteObject(String key) {
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }

    @Override
    public String toCdnUrl(String key) {
        return "https://" + cdnDomain + "/" + key;
    }

    @Override
    public void uploadObject(String key, byte[] data, String contentType) {
        try {
            log.info("Uploading object to S3: key={}, size={}, contentType={}", key, data.length, contentType);
            
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .contentLength((long) data.length)
                    .acl(ObjectCannedACL.PRIVATE)
                    .build();

            s3.putObject(putRequest, RequestBody.fromBytes(data));
            
            log.info("Successfully uploaded object to S3: {}", key);
        } catch (Exception e) {
            log.error("Error uploading object to S3: key={}", key, e);
            throw new RuntimeException("Failed to upload object to S3: " + e.getMessage(), e);
        }
    }
}
