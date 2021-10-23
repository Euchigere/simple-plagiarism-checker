package com.emmanuelc.plagiarismchecker.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SessionDTO {
    private long id;

    private String referenceStudent;

    private RespFile referenceContent;

    private String compareStudent;

    private RespFile compareContent;

    private String percentageSimilarity;

    private String result;

    private ZonedDateTime time;
}
