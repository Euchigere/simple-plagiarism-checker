package com.emmanuelc.plagiarismchecker.domain.models.enumerations;

import org.springframework.http.MediaType;

public enum MimeType {
    TEXT_PLAIN(MediaType.TEXT_PLAIN_VALUE),
    TEXT_HTML(MediaType.TEXT_HTML_VALUE);

    private final String value;

    MimeType(final String value) {
        this.value = value;
    }

    public static MimeType getEnumValue(String value) {
        for (MimeType mime: values()) {
            if (mime.value.equalsIgnoreCase(value)) {
                return mime;
            }
        }
        throw new IllegalArgumentException("Unknown value '" + value + "' for Mime enums");
    }

    @Override
    public String toString() {
        return this.value;
    }
}
