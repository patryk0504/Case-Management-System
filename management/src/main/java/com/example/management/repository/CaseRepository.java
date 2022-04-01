package com.example.management.repository;
import com.example.management.model.Severity;
import com.example.management.model.Status;
import com.example.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.management.model.Case;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaseRepository extends JpaRepository<Case, Long>{
//    List<Case> findByStatus(Status status);
//    List<Case> findBySeverity(Severity severity);
    Long removeAllByStatus(Status status);
    Optional<Case> findByUuid(UUID uuid);
}
