package com.github.omaru.transaction.validator.infrastructure.json;

import com.fasterxml.jackson.databind.MappingIterator;
import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component("recordEntryJsonReader")
@Slf4j
@RequiredArgsConstructor
public class RecordEntryJsonReader implements RecordEntryReader<InputStream> {
    private final RecordEntryJsonMapper recordEntryJsonMapper;

    @Override
    public Stream<RecordEntry> read(InputStream inputStream) {
        try {
            MappingIterator<RecordEntryJson> it =
                    recordEntryJsonMapper.getObjectMapper().readerFor(RecordEntryJson.class).readValues(inputStream);

            Spliterator<RecordEntryJson> spliterator =
                    Spliterators.spliteratorUnknownSize(it, java.util.Spliterator.ORDERED);

            return StreamSupport.stream(spliterator, false).map(recordEntryJsonMapper::toDomain)
                    .onClose(() -> {
                        try {
                            it.close();
                        } catch (IOException e) {
                            log.warn("Failed to close iterator", e);
                        }
                    });
        } catch (IOException e) {
            log.error("Failed to parse input stream into RecordEntry stream", e);
            return Stream.empty();
        }
    }
}
