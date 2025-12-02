package com.github.omaru.transaction.validator.infrastructure.persistence.jpa;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.service.RecordService;
import com.github.omaru.transaction.validator.test.util.IntegrationTest;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@Sql(
        scripts = {"/db/record_entries.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class RecordJpaServiceIT {
    @Autowired
    private RecordService service;

    @Test
    void shouldThrowConstraintViolationExceptionWhenDuplicateTransactionReference() {
        var exception = assertThrows(NotUniqueReferenceNumberException.class, () -> service.save(
                RecordEntry.builder().transactionReference(1235L)
                        .accountNumber(Iban.valueOf("NL91RABO0315273637"))
                        .description("Book John Smith")
                        .startBalance(BigDecimal.valueOf(21.6))
                        .mutation(BigDecimal.valueOf(-41.83))
                        .endBalance(BigDecimal.valueOf(-20.23))
                        .build()));

        assertThat(exception).hasMessage("Reference number 1235 is not unique");
    }
}
