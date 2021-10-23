package com.emmanuelc.plagiarismchecker.api;

import com.emmanuelc.plagiarismchecker.domain.dto.ApiResponse;
import com.emmanuelc.plagiarismchecker.domain.dto.CompareReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.CompareRespDTO;
import com.emmanuelc.plagiarismchecker.service.CompareService;
import com.emmanuelc.plagiarismchecker.util.ControllerUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/compare")
public class CompareApi {
    private final CompareService compareService;

    /**
     * End-point to process compare request and create a compare session
     * @param requestDTO contains all the needed information for content comparison
     * @return CompareRespDTO
     * @throws IOException
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CompareRespDTO>> compareContent(@ModelAttribute @Valid CompareReqDTO requestDTO) throws IOException {
        final CompareRespDTO responseDTO = compareService.processCompareRequest(requestDTO);
        return ControllerUtil.buildResponseEntity(responseDTO);
    }

    /**
     * End-point to rerun comparison of a session
     * @param id of session to rerun comparison on
     * @return CompareRespDTO
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CompareRespDTO>> compareContent(@PathVariable long id) {
        final CompareRespDTO responseDTO = compareService.processCompareRequest(id);
        return ControllerUtil.buildResponseEntity(responseDTO);
    }

}
