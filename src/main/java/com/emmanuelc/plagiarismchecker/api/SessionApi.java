package com.emmanuelc.plagiarismchecker.api;

import com.emmanuelc.plagiarismchecker.domain.dto.ApiResponse;
import com.emmanuelc.plagiarismchecker.domain.dto.SessionDTO;
import com.emmanuelc.plagiarismchecker.domain.mapper.ComparisonSessionMapper;
import com.emmanuelc.plagiarismchecker.exceptions.ResourceNotFoundException;
import com.emmanuelc.plagiarismchecker.repository.SessionRepo;
import com.emmanuelc.plagiarismchecker.util.ControllerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionApi {
    private final SessionRepo sessionRepo;

    private final ComparisonSessionMapper sessionMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SessionDTO>>> getSessions(@PageableDefault Pageable pageable) {
        final Page<SessionDTO> sessionDTOPage = sessionRepo.findAllByOrderByTimeDesc(pageable).map(sessionMapper::toSessionDto);
        return ControllerUtil.buildResponseEntity(sessionDTOPage);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionDTO>> getSession(@PathVariable long id) {
        final SessionDTO sessionDTO = sessionRepo.findById(id)
                .map(sessionMapper::toSessionDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Session with id: '%d', not found", id)));
        return ControllerUtil.buildResponseEntity(sessionDTO);
    }
}
