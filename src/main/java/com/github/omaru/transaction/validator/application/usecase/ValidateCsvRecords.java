package com.github.omaru.transaction.validator.application.usecase;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.domain.model.Report;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;

@UseCase
public class ValidateCsvRecords {
    private final RecordEntryReader<InputStream> recordEntryReader;

    public ValidateCsvRecords(@Qualifier("recordEntryCsvReader") RecordEntryReader<InputStream> recordEntryReader) {
        this.recordEntryReader = recordEntryReader;
    }

    Report execute(InputStream inputStream) {
        var records = recordEntryReader.read(inputStream);
        try (records) {
            records.forEach(record -> {
            });
        }
        return Report.builder().build();
    }

}
