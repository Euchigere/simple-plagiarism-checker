package com.emmanuelc.plagiarismchecker.util;

import com.emmanuelc.plagiarismchecker.domain.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ControllerUtil {
    private ControllerUtil() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildResponseEntity(T data) {
        return buildResponseEntity(data, "success");
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildResponseEntity(T data, String message) {
        final ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        response.setStatus(true);
        response.setData(data);

        return ResponseEntity.ok(response);
    }
}