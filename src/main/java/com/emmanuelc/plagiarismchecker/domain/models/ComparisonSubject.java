package com.emmanuelc.plagiarismchecker.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comparison_subject")
public class ComparisonSubject {
    @Id
    private long id;

    @Column(nullable = false)
    private String name;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private DBFile content;
}
