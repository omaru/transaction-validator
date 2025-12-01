package com.github.omaru.transaction.validator.infrastructure.csv;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

@Component
public class RecordEntryCsvMapper {
    public RecordEntry toDomain(RecordEntryCsv recordEntryCsv) {
        return RecordEntry.builder()
                .transactionReference(recordEntryCsv.getTransactionReference())
                .accountNumber(Iban.valueOf(recordEntryCsv.getAccountNumber()))
                .startBalance(recordEntryCsv.getStartBalance())
                .mutation(recordEntryCsv.getMutation())
                .description(recordEntryCsv.getDescription())
                .endBalance(recordEntryCsv.getEndBalance())
                .build();
    }
}
