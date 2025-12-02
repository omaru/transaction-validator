package com.github.omaru.transaction.validator.infrastructure.repository;

import com.github.omaru.transaction.validator.infrastructure.config.IntegrationTest;
import com.github.omaru.transaction.validator.infrastructure.persistence.entity.RecordEntity;
import com.github.omaru.transaction.validator.infrastructure.persistence.jpa.RecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
public class RecordRepositoryIT {
    @Autowired
    private RecordRepository recordRepository;

    @AfterEach
    void tearDown() {
        recordRepository.deleteAll();
    }

    @Test
    void shouldSaveRecordEntity() {
        var recordEntity = RecordEntity.builder().transactionReference(123L).startBalance(BigDecimal.valueOf(123)).build();
        recordRepository.save(recordEntity);
        recordRepository.findById(recordEntity.getId()).orElseThrow(() -> new RuntimeException("Record with ID:" +
                recordEntity.getId() + " not found"));
    }

    @Test
    void shouldAcceptOnlyUniqueTransactionReferences() {
        var originalReference = RecordEntity.builder().transactionReference(123L).startBalance(BigDecimal.valueOf(123)).build();
        var duplicateReference = RecordEntity.builder().transactionReference(123L).startBalance(BigDecimal.valueOf(123)).build();

        recordRepository.save(originalReference);

        var exception = assertThrows(DataIntegrityViolationException.class, () -> recordRepository.save(duplicateReference));

        assertThat(exception.getMessage()).contains("transaction_reference)=(123) already exists.");
    }
}
