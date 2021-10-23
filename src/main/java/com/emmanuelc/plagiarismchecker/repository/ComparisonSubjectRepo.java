package com.emmanuelc.plagiarismchecker.repository;

import com.emmanuelc.plagiarismchecker.domain.models.ComparisonSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComparisonSubjectRepo extends JpaRepository<ComparisonSubject, Long> {
}
