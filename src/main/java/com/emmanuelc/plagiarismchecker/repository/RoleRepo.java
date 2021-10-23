package com.emmanuelc.plagiarismchecker.repository;

import com.emmanuelc.plagiarismchecker.domain.models.Role;
import com.emmanuelc.plagiarismchecker.domain.models.enumerations.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
