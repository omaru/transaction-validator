package com.github.omaru.transaction.validator.infrastructure.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.omaru.transaction.validator.infrastructure.config.JacksonConfig;
import org.iban4j.IbanFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordEntryJsonMapperTest {
    private RecordEntryJsonMapper mapper;
    private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();

    @BeforeEach
    void setUp() {
        mapper = new RecordEntryJsonMapper(objectMapper);
    }

    @Test
    void shouldMapJsonToDomain() {
        RecordEntryJson recordEntryJson = new RecordEntryJson();
        recordEntryJson.setReference(194261L);
        recordEntryJson.setAccountNumber("NL91RABO0315273637");
        recordEntryJson.setDescription("Book John Smith");
        recordEntryJson.setStartBalance(new BigDecimal("21.6"));
        recordEntryJson.setMutation(new BigDecimal("-41.83"));
        recordEntryJson.setEndBalance(new BigDecimal("-20.23"));

        var recordEntry = mapper.toDomain(recordEntryJson);

        assertThat(recordEntry.getTransactionReference()).isEqualTo(194261L);
        assertThat(recordEntry.getAccountNumber().toString()).isEqualTo("NL91RABO0315273637");
        assertThat(recordEntry.getDescription()).isEqualTo("Book John Smith");
        assertThat(recordEntry.getStartBalance()).isEqualTo(new BigDecimal("21.6"));
        assertThat(recordEntry.getMutation()).isEqualTo(new BigDecimal("-41.83"));
        assertThat(recordEntry.getEndBalance()).isEqualTo(new BigDecimal("-20.23"));
    }

    @Test
    void shouldThrowExceptionIfIbanIsNotValid() {
        RecordEntryJson recordEntryJson = new RecordEntryJson();
        recordEntryJson.setAccountNumber("invalidiban");

        assertThrows(IbanFormatException.class, () -> mapper.toDomain(recordEntryJson));
    }

}
