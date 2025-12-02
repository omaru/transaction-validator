package com.github.omaru.transaction.validator.infrastructure.persistence.jpa.mapper;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.infrastructure.persistence.entity.RecordEntity;
import com.github.omaru.transaction.validator.infrastructure.persistence.jpa.mappper.RecordEntryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RecordEntryMapperTest {
    private RecordEntryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecordEntryMapper();
    }

    @Test
    void shouldMapEntityToDomain() {
        var entity = RecordEntity.builder()
                .id(UUID.fromString("86c02577-cfec-44e5-8b73-14864c2e24a3"))
                .transactionReference(1234L)
                .accountNumber("NL91ABNA0417164300")
                .description("Test description")
                .startBalance(BigDecimal.valueOf(12.00))
                .mutation(BigDecimal.valueOf(5.00))
                .endBalance(BigDecimal.valueOf(17.00))
                .build();
        var domain = mapper.toDomain(entity);

        assertThat(domain).isEqualTo(
                RecordEntry.builder()
                        .id(UUID.fromString("86c02577-cfec-44e5-8b73-14864c2e24a3"))
                        .transactionReference(1234L)
                        .accountNumber(org.iban4j.Iban.valueOf("NL91ABNA0417164300"))
                        .description("Test description")
                        .startBalance(BigDecimal.valueOf(12.00))
                        .mutation(BigDecimal.valueOf(5.00))
                        .endBalance(BigDecimal.valueOf(17.00))
                        .build()
        );
    }

    @Test
    void shouldMapDomainToEntity() {
        var domain = RecordEntry.builder()
                .id(UUID.fromString("86c02577-cfec-44e5-8b73-14864c2e24a3"))
                .transactionReference(1234L)
                .accountNumber(org.iban4j.Iban.valueOf("NL91ABNA0417164300"))
                .description("Test description")
                .startBalance(BigDecimal.valueOf(12.00))
                .mutation(BigDecimal.valueOf(5.00))
                .endBalance(BigDecimal.valueOf(17.00))
                .build();

        var entity = mapper.toEntity(domain);

        assertThat(entity).isEqualTo(
                RecordEntity.builder()
                        .id(UUID.fromString("86c02577-cfec-44e5-8b73-14864c2e24a3"))
                        .transactionReference(1234L)
                        .accountNumber("NL91ABNA0417164300")
                        .description("Test description")
                        .startBalance(BigDecimal.valueOf(12.00))
                        .mutation(BigDecimal.valueOf(5.00))
                        .endBalance(BigDecimal.valueOf(17.00))
                        .build()
        );
    }
}
