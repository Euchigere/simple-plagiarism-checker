package com.emmanuelc.plagiarismchecker.repository;

import com.emmanuelc.plagiarismchecker.domain.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepo extends JpaRepository<BlacklistedToken, Long> {
    BlacklistedToken findByToken(String token);
}
