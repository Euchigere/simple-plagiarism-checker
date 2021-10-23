package com.emmanuelc.plagiarismchecker.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "token_blacklist")
public class BlacklistedToken extends AuditingEntity {
    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Date expiration;
}
