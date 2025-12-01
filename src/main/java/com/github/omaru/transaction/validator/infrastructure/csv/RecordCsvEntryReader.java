package com.github.omaru.transaction.validator.infrastructure.csv;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordCsvEntryReader implements RecordEntryReader<InputStream> {
    private final RecordEntryCsvMapper recordEntryCsvMapper;

    @Override
    public Stream<RecordEntry> read(InputStream inputStream) {
        Reader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );
        Iterator<RecordEntryCsv> iterator = new CsvToBeanBuilder<RecordEntryCsv>(reader)
                .withType(RecordEntryCsv.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .iterator();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
                false
        ).map(recordEntryCsvMapper::toDomain).onClose(() -> {
            try {
                reader.close();
            } catch (IOException e) {
                log.warn("Failed to close reader", e);
            }
        });
    }
}
