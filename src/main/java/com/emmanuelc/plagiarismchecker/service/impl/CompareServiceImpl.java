package com.emmanuelc.plagiarismchecker.service.impl;

import com.emmanuelc.plagiarismchecker.domain.dto.CompareReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.CompareRespDTO;
import com.emmanuelc.plagiarismchecker.domain.mapper.ComparisonSessionMapper;
import com.emmanuelc.plagiarismchecker.domain.models.ComparisonSession;
import com.emmanuelc.plagiarismchecker.domain.models.DBFile;
import com.emmanuelc.plagiarismchecker.exceptions.AppCustomException;
import com.emmanuelc.plagiarismchecker.exceptions.ResourceNotFoundException;
import com.emmanuelc.plagiarismchecker.repository.SessionRepo;
import com.emmanuelc.plagiarismchecker.service.CompareService;
import com.emmanuelc.plagiarismchecker.service.DiffMatchPatch;
import com.emmanuelc.plagiarismchecker.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompareServiceImpl implements CompareService {
    private final ComparisonSessionMapper sessionMapper;

    private final SessionRepo sessionRepo;

    @Override
    public CompareRespDTO processCompareRequest(final CompareReqDTO requestDTO) throws IOException {
        final String identifier = getIdentifier(requestDTO);
        final Map<String, Object> resultMap = compare(requestDTO.getReferenceContent(), requestDTO.getCompareContent(), identifier);
        final ComparisonSession session = sessionMapper.toPartialEntity(requestDTO);

        return saveSession(resultMap, session);
    }

    @Override
    public CompareRespDTO processCompareRequest(long id) {
        final ComparisonSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Session with id: %d not found", id)));
        final Map<String, Object> resultMap = compare(session.getReference().getContent(), session.getCompare().getContent());

        return saveSession(resultMap, session);
    }

    private CompareRespDTO saveSession(final Map<String, Object> resultMap, final ComparisonSession session) {
        session.setResult((String) resultMap.get(AppConstants.RESULT));
        session.setSimilarity((Double) resultMap.get(AppConstants.PERCENTAGE_SIMILARITY));
        session.setTime(Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()));

        return sessionMapper.toCompareResponseDto(sessionRepo.save(session));
    }

    Map<String, Object> compare(
            final MultipartFile referenceFile,
            final MultipartFile compareFile,
            final String identifier
    ) throws IOException {
        Optional<ComparisonSession> optional = sessionRepo.findByIdentifier(identifier);
        if (optional.isPresent()) {
            throw new AppCustomException("Request could not be processed because it already exists", HttpStatus.CONFLICT);
        }
        final String referenceText = new String(referenceFile.getBytes(), StandardCharsets.UTF_8);
        final String compareText = new String(compareFile.getBytes(), StandardCharsets.UTF_8);

        return compare(referenceText, compareText);
    }

    Map<String, Object> compare(final DBFile referenceFile, final DBFile compareFile) {
        final String referenceText = new String(referenceFile.getData(), StandardCharsets.UTF_8);
        final String compareText = new String(compareFile.getData(), StandardCharsets.UTF_8);

        return compare(referenceText, compareText);
    }

    private String getIdentifier(final CompareReqDTO requestDTO) {
        return ComparisonSessionMapper.generateIdentifier(requestDTO);
    }

    Map<String, Object> compare(final String referenceText, final String compareText) {
        final DiffMatchPatch dmp = new DiffMatchPatch();

        LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(referenceText, compareText);
        dmp.diffCleanupSemantic(diffs);
        final String htmlString = dmp.diffPrettyHtml(diffs);
        final double percentageSimilarity = calcPercentageSimilarity(
                referenceText.length(),
                compareText.length(),
                dmp.diffLevenshtein(diffs)
        );

        return new HashMap<>() {
            {
                this.put(AppConstants.PERCENTAGE_SIMILARITY, percentageSimilarity);
                this.put(AppConstants.RESULT, htmlString);
            }
        };
    }

    double calcPercentageSimilarity(int referenceTextLength, int compareTextLength, int levenshteinDistance) {
        final int totalLength = referenceTextLength + compareTextLength;
        final int delta = totalLength - levenshteinDistance;
        if (delta == 0) {
            return 0;
        }
        return (delta / (totalLength + 0.00)) * 100;
    }
}
