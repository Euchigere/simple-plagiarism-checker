package com.emmanuelc.plagiarismchecker.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareRespDTO {
    private String percentageSimilarity;

    private String result;
}
