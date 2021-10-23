package com.emmanuelc.plagiarismchecker.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "comparison_session")
public class ComparisonSession extends AuditingEntity {
    @Column(nullable = false, unique = true)
    private String identifier;

    @Column(nullable = false)
    private double similarity;

    @Column(nullable = false)
    private Timestamp time;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reference_subject_id", nullable = false)
    private ComparisonSubject reference;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "compare_subject_id", nullable = false)
    private ComparisonSubject compare;

    @Lob
    @Column(nullable = false)
    private String result;
}
