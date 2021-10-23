package com.emmanuelc.plagiarismchecker.domain.dto;

import com.emmanuelc.plagiarismchecker.validation.ValidFileType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CompareReqDTO {
    @NotBlank
    private String referenceStudent;

    @NotNull
    @ValidFileType
    private MultipartFile referenceContent;

    @NotBlank
    private String compareStudent;

    @NotNull
    @ValidFileType
    private MultipartFile compareContent;
}
