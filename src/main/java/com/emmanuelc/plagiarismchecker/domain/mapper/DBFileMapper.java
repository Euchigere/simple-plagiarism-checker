package com.emmanuelc.plagiarismchecker.domain.mapper;

import com.emmanuelc.plagiarismchecker.api.DBFileApi;
import com.emmanuelc.plagiarismchecker.domain.dto.RespFile;
import com.emmanuelc.plagiarismchecker.domain.models.DBFile;
import com.emmanuelc.plagiarismchecker.domain.models.enumerations.MimeType;
import com.emmanuelc.plagiarismchecker.repository.DBFileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
@RequiredArgsConstructor
public class DBFileMapper {
    private final DBFileRepo fileRepo;

    public DBFile toEntity(MultipartFile dto) {
        if (dto == null) {
            return null;
        }
        final DBFile file = new DBFile();
        file.setName(StringUtils.cleanPath(dto.getOriginalFilename()));
        file.setType(MimeType.getEnumValue(dto.getContentType()));
        try {
            file.setData(dto.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return fileRepo.save(file);
    }

    public RespFile toResponseFileDto(DBFile entity) {
        if (entity == null) {
            return null;
        }
        final RespFile file = new RespFile();
        file.setName(entity.getName());
        file.setType(entity.getType());
        file.setSize(entity.getData().length);
        file.setUrl(
                MvcUriComponentsBuilder
                        .fromMethodName(
                                DBFileApi.class,
                                "getFile",
                                entity.getId())
                        .toUriString()
        );
        return file;
    }
}
