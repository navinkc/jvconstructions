package com.backend.jvconstructions.controller;

import com.backend.jvconstructions.enums.ProjectRole;
import com.backend.jvconstructions.util.MediaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@PreAuthorize("hasRole('" + ProjectRole.ADMIN + "')")
@RequiredArgsConstructor
public class DebugController {

    private final MediaStorage mediaStorage;

    @GetMapping("/debug")
    public Map<String, Object> debug(@AuthenticationPrincipal Jwt jwt, Authentication auth) {
        return Map.of(
                "claims", jwt.getClaims(),
                "authorities", auth.getAuthorities()
        );
    }

    @GetMapping("/aws-test")
    public Map<String, Object> testAws() {
        try {
            // Test basic S3 connectivity
            String testKey = "test/connection-test.txt";
            String testUrl = mediaStorage.toCdnUrl(testKey);
            return Map.of(
                "status", "success",
                "message", "AWS S3 connection test successful",
                "testUrl", testUrl
            );
        } catch (Exception e) {
            return Map.of(
                "status", "error",
                "message", "AWS S3 connection test failed: " + e.getMessage(),
                "error", e.getClass().getSimpleName()
            );
        }
    }

    @GetMapping("/aws-test-public")
    @PreAuthorize("permitAll()")
    public Map<String, Object> testAwsPublic() {
        try {
            // Test basic S3 connectivity without authentication
            String testKey = "test/connection-test.txt";
            String testUrl = mediaStorage.toCdnUrl(testKey);
            return Map.of(
                "status", "success",
                "message", "AWS S3 connection test successful",
                "testUrl", testUrl,
                "bucket", "jvconstructions-media"
            );
        } catch (Exception e) {
            return Map.of(
                "status", "error",
                "message", "AWS S3 connection test failed: " + e.getMessage(),
                "error", e.getClass().getSimpleName(),
                "stackTrace", e.getStackTrace()[0].toString()
            );
        }
    }
}
