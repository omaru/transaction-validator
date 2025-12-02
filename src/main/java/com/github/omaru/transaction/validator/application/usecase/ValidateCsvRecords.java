package com.github.omaru.transaction.validator.application.usecase;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.application.usecase.exception.UnableToReadCsvException;
import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.Reason;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.model.Report;
import com.github.omaru.transaction.validator.domain.service.RecordService;
import com.github.omaru.transaction.validator.domain.service.RecordValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.omaru.transaction.validator.domain.service.RecordValidator.VALID_RECORD;

@UseCase
public class ValidateCsvRecords {
    private final RecordEntryReader<InputStream> recordEntryReader;
    private final RecordValidator validator;
    private final RecordService recordService;
    private static final List<String> ACCEPTED_CONTENT_TYPES = List.of(
            "text/csv",
            "application/csv",
            "application/vnd.ms-excel"
    );
    private static final String CSV_EXTENSION = ".csv";

    public ValidateCsvRecords(@Qualifier("recordEntryCsvReader") RecordEntryReader<InputStream> recordEntryReader,
                              RecordValidator validator,
                              RecordService recordService) {
        this.recordEntryReader = recordEntryReader;
        this.validator = validator;
        this.recordService = recordService;
    }

    public Report execute(MultipartFile file) {
        if (!isCsv(file)) {
            throw new UnableToReadCsvException("File is not a valid CSV");
        }
        try (InputStream inputStream = file.getInputStream()) {
            try (var records = recordEntryReader.read(inputStream)) {
                List<RecordEntry> entries = records.toList();
                Integer totalRecordsRead = entries.size();
                List<FailedRecord> failedRecords = entries.stream()
                        .map(this::processFailedRecords)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                recordService.deleteAll();
                return Report.builder().totalRecordsRead(totalRecordsRead)
                        .totalFailedRecords(failedRecords.size())
                        .failedRecords(failedRecords).build();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FailedRecord processFailedRecords(RecordEntry record) {
        var balanceResult = validator.validateEndBalance(record);
        var uniqueTransactionResult = validator.validateUniqueTransactionReference(record);
        if (balanceResult != VALID_RECORD) {
            var reasons = new ArrayList<Reason>();
            reasons.addAll(balanceResult.getReasons());
            reasons.addAll(uniqueTransactionResult != VALID_RECORD ? uniqueTransactionResult.getReasons() : Collections.emptyList());
            return FailedRecord.builder()
                    .record(record)
                    .reasons(reasons)
                    .build();
        }
        return uniqueTransactionResult;
    }

    private boolean isCsv(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            String ct = contentType.toLowerCase(Locale.ROOT);
            if (ACCEPTED_CONTENT_TYPES.stream().anyMatch(ac -> ac.contains(ct))) {
                return true;
            }
        }
        String filename = file.getOriginalFilename();
        return filename != null && filename.toLowerCase(Locale.ROOT).endsWith(CSV_EXTENSION);
    }

}
