package com.github.omaru.transaction.validator.application.usecase;

import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.model.Report;
import com.github.omaru.transaction.validator.domain.service.RecordValidator;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UseCase
@RequiredArgsConstructor
public class GenerateRecordReport {
    private final RecordValidator validator;

    public Report execute(Stream<RecordEntry> records) {
        List<RecordEntry> entries = records.toList();
        Integer totalRecordsRead = entries.size();
        final Set<Long> seenTransactionReferences = new HashSet<>();
        List<FailedRecord> failedRecords = entries.stream()
                .map(record -> validator.validate(record, seenTransactionReferences))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return Report.builder().totalRecordsRead(totalRecordsRead)
                .totalFailedRecords(failedRecords.size())
                .failedRecords(failedRecords).build();
    }
}
