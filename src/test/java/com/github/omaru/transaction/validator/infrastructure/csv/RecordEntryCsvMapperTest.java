package com.github.omaru.transaction.validator.infrastructure.csv;

import org.iban4j.IbanFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordEntryCsvMapperTest {
    private RecordEntryCsvMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecordEntryCsvMapper();
    }

    @Test
    void shouldMapCsvToDomain() {
        RecordEntryCsv recordEntryCsv = new RecordEntryCsv();
        recordEntryCsv.setReference(194261L);
        recordEntryCsv.setAccountNumber("NL91RABO0315273637");
        recordEntryCsv.setDescription("Book John Smith");
        recordEntryCsv.setStartBalance(new java.math.BigDecimal("21.6"));
        recordEntryCsv.setMutation(new java.math.BigDecimal("-41.83"));
        recordEntryCsv.setEndBalance(new java.math.BigDecimal("-20.23"));

        var recordEntry = mapper.toDomain(recordEntryCsv);

        assertThat(recordEntry.getTransactionReference()).isEqualTo(194261L);
        assertThat(recordEntry.getAccountNumber().toString()).isEqualTo("NL91RABO0315273637");
        assertThat(recordEntry.getDescription()).isEqualTo("Book John Smith");
        assertThat(recordEntry.getStartBalance()).isEqualTo(new java.math.BigDecimal("21.6"));
        assertThat(recordEntry.getMutation()).isEqualTo(new java.math.BigDecimal("-41.83"));
        assertThat(recordEntry.getEndBalance()).isEqualTo(new java.math.BigDecimal("-20.23"));
    }

    @Test
    void shouldThrowExceptionIfIbanIsNotValid() {
        RecordEntryCsv recordEntryCsv = new RecordEntryCsv();
        recordEntryCsv.setAccountNumber("invalidiban");

        assertThrows(IbanFormatException.class, () -> mapper.toDomain(recordEntryCsv));
    }
}
