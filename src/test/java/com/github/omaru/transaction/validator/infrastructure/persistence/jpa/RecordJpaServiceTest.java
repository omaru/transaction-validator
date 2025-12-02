package com.github.omaru.transaction.validator.infrastructure.persistence.jpa;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.service.RecordService;
import com.github.omaru.transaction.validator.infrastructure.persistence.jpa.mappper.RecordEntryMapper;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordJpaServiceTest {
    @Mock
    private RecordRepository recordRepository;
    private RecordService service;

    @BeforeEach
    void setUp() {
        this.service = new RecordJpaService(recordRepository, new RecordEntryMapper());
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionIfNotMessageIsSent() throws NotUniqueReferenceNumberException {
        var entry = RecordEntry.builder().transactionReference(1235L)
                .accountNumber(Iban.valueOf("NL91RABO0315273637"))
                .description("Book John Smith")
                .startBalance(BigDecimal.valueOf(21.6))
                .mutation(BigDecimal.valueOf(-41.83))
                .endBalance(BigDecimal.valueOf(-20.23))
                .build();
        when(recordRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(DataIntegrityViolationException.class, () -> service.save(entry));

    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionIfOtherMessageConstraintIsSent() throws NotUniqueReferenceNumberException {
        var entry = RecordEntry.builder().transactionReference(1235L)
                .accountNumber(Iban.valueOf("NL91RABO0315273637"))
                .description("Book John Smith")
                .startBalance(BigDecimal.valueOf(21.6))
                .mutation(BigDecimal.valueOf(-41.83))
                .endBalance(BigDecimal.valueOf(-20.23))
                .build();
        when(recordRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("other message constraint"));

        assertThrows(DataIntegrityViolationException.class, () -> service.save(entry));

    }

    @Test
    void shouldDeleteAllRecords() {

        service.deleteAll();

        verify(recordRepository).deleteAll();
    }

}
