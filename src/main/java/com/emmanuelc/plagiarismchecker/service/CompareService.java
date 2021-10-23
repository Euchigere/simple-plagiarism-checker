package com.emmanuelc.plagiarismchecker.service;

import com.emmanuelc.plagiarismchecker.domain.dto.CompareReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.CompareRespDTO;

import java.io.IOException;

public interface CompareService {
    CompareRespDTO processCompareRequest(CompareReqDTO requestDTO) throws IOException;

    CompareRespDTO processCompareRequest(long id);

}
