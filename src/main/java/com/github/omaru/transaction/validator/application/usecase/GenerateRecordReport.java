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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UseCase
@RequiredArgsConstructor
public class GenerateRecordReport {
    private final RecordValidator validator;

    public Report execute(Stream<RecordEntry> records) {
        var seenTransactionReferences = new HashSet<Long>();
        var total = new AtomicInteger();
        final List<FailedRecord> failedRecords = records
                .peek(r -> total.incrementAndGet())
                .map(record -> validator.validate(record, seenTransactionReferences))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return Report.builder().totalRecordsRead(total.get())
                .totalFailedRecords(failedRecords.size())
                .failedRecords(failedRecords).build();
    }
}
