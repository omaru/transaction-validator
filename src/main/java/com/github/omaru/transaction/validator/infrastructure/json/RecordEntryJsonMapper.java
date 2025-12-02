package com.github.omaru.transaction.validator.infrastructure.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import lombok.RequiredArgsConstructor;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordEntryJsonMapper {
    private final ObjectMapper objectMapper;

    public RecordEntry toDomain(RecordEntryJson entry) {
        return RecordEntry.builder()
                .transactionReference(entry.getReference())
                .accountNumber(Iban.valueOf(entry.getAccountNumber()))
                .startBalance(entry.getStartBalance())
                .mutation(entry.getMutation())
                .description(entry.getDescription())
                .endBalance(entry.getEndBalance())
                .build();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
