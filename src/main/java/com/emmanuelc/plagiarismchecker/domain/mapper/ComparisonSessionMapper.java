package com.emmanuelc.plagiarismchecker.domain.mapper;

import com.emmanuelc.plagiarismchecker.domain.dto.CompareReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.CompareRespDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.SessionDTO;
import com.emmanuelc.plagiarismchecker.domain.models.ComparisonSession;
import com.emmanuelc.plagiarismchecker.domain.models.ComparisonSubject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class ComparisonSessionMapper {
    private final DBFileMapper fileMapper;

    public ComparisonSession toPartialEntity(CompareReqDTO dto) {
        if (dto == null) {
            return null;
        }
        final ComparisonSubject reference = new ComparisonSubject();
        reference.setName(dto.getReferenceStudent());
        reference.setContent(fileMapper.toEntity(dto.getReferenceContent()));

        final ComparisonSubject compare = new ComparisonSubject();
        compare.setName(dto.getCompareStudent());
        compare.setContent(fileMapper.toEntity(dto.getCompareContent()));

        final ComparisonSession session = new ComparisonSession();
        session.setIdentifier(ComparisonSessionMapper.generateIdentifier(dto));
        session.setReference(reference);
        session.setCompare(compare);

        return session;
    }

    public CompareRespDTO toCompareResponseDto(final ComparisonSession entity) {
        if (entity == null) {
            return null;
        }
        final CompareRespDTO responseDTO = new CompareRespDTO();
        responseDTO.setPercentageSimilarity(displaySimilarity(entity.getSimilarity()));
        responseDTO.setResult(entity.getResult());

        return responseDTO;
    }

    public SessionDTO toSessionDto(final ComparisonSession entity) {
        if (entity == null) {
            return null;
        }
        final SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(entity.getId());
        sessionDTO.setResult(entity.getResult());
        sessionDTO.setPercentageSimilarity(displaySimilarity(entity.getSimilarity()));
        sessionDTO.setTime(ZonedDateTime.ofInstant(entity.getTime().toInstant(), ZoneOffset.UTC));

        sessionDTO.setReferenceStudent(entity.getReference().getName());
        sessionDTO.setReferenceContent(fileMapper.toResponseFileDto(entity.getReference().getContent()));

        sessionDTO.setCompareStudent(entity.getCompare().getName());
        sessionDTO.setCompareContent(fileMapper.toResponseFileDto(entity.getCompare().getContent()));

        return sessionDTO;
    }

    static String displaySimilarity(double similarity) {
        return String.format("%.2f%%", similarity);
    }

    public static String generateIdentifier(final CompareReqDTO dto) {
        final String referenceStudent = dto.getReferenceStudent();
        final String compareStudent = dto.getCompareStudent();
        final MultipartFile referenceFile = dto.getReferenceContent();
        final MultipartFile compareFile = dto.getCompareContent();
        return String.format(
                "%s:%s:%s:%d-%s%s:%s%d",
                referenceStudent,
                referenceFile.getName(),
                referenceFile.getContentType(),
                referenceFile.getSize(),
                compareStudent,
                compareFile.getName(),
                compareFile.getContentType(),
                compareFile.getSize()
        );
    }
}
