package com.github.omaru.transaction.validator.infrastructure.persistence.jpa;

import com.github.omaru.transaction.validator.infrastructure.persistence.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {
}
