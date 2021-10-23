package com.emmanuelc.plagiarismchecker.repository;

import com.emmanuelc.plagiarismchecker.domain.models.ComparisonSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<ComparisonSession, Long> {
    Optional<ComparisonSession> findByIdentifier(String identifier);

    Page<ComparisonSession> findAllByOrderByTimeDesc(Pageable pageable);
}
