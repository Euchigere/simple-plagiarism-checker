package com.emmanuelc.plagiarismchecker.domain.dto;

import com.emmanuelc.plagiarismchecker.domain.models.enumerations.MimeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespFile {
    private String name;

    private String url;

    private MimeType type;

    private long size;
}
