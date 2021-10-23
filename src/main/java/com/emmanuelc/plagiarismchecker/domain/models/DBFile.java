package com.emmanuelc.plagiarismchecker.domain.models;

import com.emmanuelc.plagiarismchecker.domain.models.enumerations.MimeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "df_file")
public class DBFile extends AuditingEntity {
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MimeType type;

    @Lob
    @Column(nullable = false)
    private byte[] data;
}
