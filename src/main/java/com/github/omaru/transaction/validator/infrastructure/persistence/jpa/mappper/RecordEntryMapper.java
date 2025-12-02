package com.github.omaru.transaction.validator.infrastructure.persistence.jpa.mappper;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.infrastructure.persistence.entity.RecordEntity;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RecordEntryMapper {

    public RecordEntry toDomain(RecordEntity entity) {
        return RecordEntry.builder()
                .id(entity.getId())
                .transactionReference(entity.getTransactionReference())
                .accountNumber(Iban.valueOf(entity.getAccountNumber()))
                .description(entity.getDescription())
                .startBalance(entity.getStartBalance())
                .mutation(entity.getMutation())
                .endBalance(entity.getEndBalance())
                .build();
    }

    public RecordEntity toEntity(RecordEntry domain) {
        return RecordEntity.builder()
                .id(domain.getId() != null ? domain.getId() : UUID.randomUUID())
                .transactionReference(domain.getTransactionReference())
                .accountNumber(domain.getAccountNumber().toString())
                .description(domain.getDescription())
                .startBalance(domain.getStartBalance())
                .mutation(domain.getMutation())
                .endBalance(domain.getEndBalance())
                .build();
    }
}
