package com.emmanuelc.plagiarismchecker.repository;

import com.emmanuelc.plagiarismchecker.domain.models.Privilege;
import com.emmanuelc.plagiarismchecker.domain.models.enumerations.PrivilegeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    Privilege findByName(PrivilegeEnum name);
}
