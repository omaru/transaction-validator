package com.github.omaru.transaction.validator.infrastructure.persistence.jpa;

import com.github.omaru.transaction.validator.test.util.IntegrationTest;
import com.github.omaru.transaction.validator.infrastructure.persistence.entity.RecordEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@Sql(
        scripts = {"/db/record_entries.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
public class RecordRepositoryIT {
    @Autowired
    private RecordRepository recordRepository;

    @AfterEach
    void tearDown() {
        recordRepository.deleteAll();
    }

    @Test
    void shouldDeleteExistingEntries() {
        recordRepository.deleteAll();
        assertThat(recordRepository.findAll()).isEmpty();
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
