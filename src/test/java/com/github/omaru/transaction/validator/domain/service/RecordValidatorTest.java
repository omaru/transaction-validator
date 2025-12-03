package com.github.omaru.transaction.validator.domain.service;

import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.Reason;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import lombok.SneakyThrows;
import lombok.val;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RecordValidatorTest {
    private RecordValidator validator;


    @BeforeEach
    void setUp() {
        validator = new RecordValidator();
    }

    @Test
    void shouldMergeValidationsOnValidate() {
        var recordEntry = RecordEntry.builder()
                .id(UUID.randomUUID())
                .transactionReference(194261L)
                .accountNumber(Iban.valueOf("NL91RABO0315273637"))
                .description("Book John Smith")
                .startBalance(BigDecimal.valueOf(21.6))
                .mutation(BigDecimal.valueOf(-3.4))
                .endBalance(BigDecimal.valueOf(11.1))
                .build();

        var seenReferences = new HashSet<Long>();
        seenReferences.add(194261L);

        var result = validator.validate(recordEntry, seenReferences);

        assertThat(result.getReasons()).containsAll(List.of(Reason.INCORRECT_END_BALANCE, Reason.DUPLICATE_REFERENCE));

    }

    @ParameterizedTest
    @MethodSource("balanceRecordsProvider")
    void shouldValidateEndBalance(RecordEntry record, boolean isValid) {
        var result = validator.validateEndBalance(record);

        if (isValid) {
            assertThat(result).isEqualTo(RecordValidator.VALID_RECORD);
        } else {
            assertThat(result).isNotEqualTo(RecordValidator.VALID_RECORD);
            assertThat(result.getReasons()).hasSize(1);
            assertThat(result.getReasons().getFirst()).isEqualTo(Reason.INCORRECT_END_BALANCE);
        }

    }

    @SneakyThrows
    @Test
    void shouldValidateUniqueTransactionReference() {
        var recordEntry = RecordEntry.builder()
                .id(UUID.randomUUID())
                .transactionReference(194261L)
                .accountNumber(Iban.valueOf("NL91RABO0315273637"))
                .description("Book John Smith")
                .startBalance(BigDecimal.valueOf(21.6))
                .mutation(BigDecimal.valueOf(-41.83))
                .endBalance(BigDecimal.valueOf(-20.23))
                .build();
        var seenReferences = new HashSet<Long>();
        seenReferences.add(recordEntry.getTransactionReference());

        val result = validator.validateUniqueTransactionReference(recordEntry, seenReferences);

        assertThat(result).isEqualTo(FailedRecord.builder()
                .record(recordEntry)
                .reason(Reason.DUPLICATE_REFERENCE).build());
    }

    @ParameterizedTest
    @MethodSource("balanceRecordsProvider")
    void shouldValidateUniqueTransaction(RecordEntry record, boolean isValid) {
        var result = validator.validateEndBalance(record);

        if (isValid) {
            assertThat(result).isEqualTo(RecordValidator.VALID_RECORD);
        } else {
            assertThat(result).isNotEqualTo(RecordValidator.VALID_RECORD);
            assertThat(result.getReasons()).hasSize(1);
            assertThat(result.getReasons().getFirst()).isEqualTo(Reason.INCORRECT_END_BALANCE);
        }

    }

    private static Stream<Arguments> balanceRecordsProvider() {
        return Stream.of(
                Arguments.of(RecordEntry.builder().transactionReference(194261L)
                        .accountNumber(Iban.valueOf("NL91RABO0315273637"))
                        .description("Book John Smith")
                        .startBalance(BigDecimal.valueOf(21.6))
                        .mutation(BigDecimal.valueOf(-41.83))
                        .endBalance(BigDecimal.valueOf(-20.23))
                        .build(), true),
                Arguments.of(RecordEntry.builder().transactionReference(194262L)
                        .accountNumber(Iban.valueOf("NL74ABNA0248990274"))
                        .description("John Doe")
                        .startBalance(BigDecimal.valueOf(21.6))
                        .mutation(BigDecimal.valueOf(-3.4))
                        .endBalance(BigDecimal.valueOf(30.23))
                        .build(), false)
        );
    }

}
