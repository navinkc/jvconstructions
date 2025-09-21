package com.backend.jvconstructions.exception;

import java.time.Instant;

public record ApiError(int status, String error, String code, String message, String path, Instant timestamp) {
}
