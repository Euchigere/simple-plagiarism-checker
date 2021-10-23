package com.emmanuelc.plagiarismchecker.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;

    private String message;

    private boolean status;

    private T data;

    public ApiResponse() {
        this.timestamp = ZonedDateTime.now(ZoneOffset.UTC);
    }
}
