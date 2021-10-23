package com.emmanuelc.plagiarismchecker.api;

import com.emmanuelc.plagiarismchecker.domain.models.DBFile;
import com.emmanuelc.plagiarismchecker.exceptions.ResourceNotFoundException;
import com.emmanuelc.plagiarismchecker.repository.DBFileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class DBFileApi {
    private final DBFileRepo fileRepo;

    /**
     * End-point to download uploaded content from database
     * @param id content unique id
     * @return resource content
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable long id) {
        final DBFile file = fileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("content with id: '%d' not found", id)));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }
}
