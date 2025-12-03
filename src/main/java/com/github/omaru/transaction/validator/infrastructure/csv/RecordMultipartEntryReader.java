package com.github.omaru.transaction.validator.infrastructure.csv;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.application.usecase.exception.UnableToReadCsvException;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Component
public class RecordMultipartEntryReader implements RecordEntryReader<MultipartFile> {
    private static final List<String> ACCEPTED_CONTENT_TYPES = List.of(
            "text/csv",
            "application/csv",
            "application/vnd.ms-excel"
    );
    private static final String CSV_EXTENSION = ".csv";

    private final RecordEntryReader<InputStream> recordEntryReader;

    public RecordMultipartEntryReader(@Qualifier("recordEntryCsvReader") RecordEntryReader<InputStream> recordEntryReader) {
        this.recordEntryReader = recordEntryReader;
    }

    @Override
    public Stream<RecordEntry> read(MultipartFile file) {
        if (!isCsv(file)) {
            throw new UnableToReadCsvException("File is not a valid CSV");
        }
        try {
            InputStream is = file.getInputStream();
            Stream<RecordEntry> stream = recordEntryReader.read(is);
            return stream.onClose(() -> {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
