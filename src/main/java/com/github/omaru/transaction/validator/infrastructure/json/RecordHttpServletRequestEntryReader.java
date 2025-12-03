package com.github.omaru.transaction.validator.infrastructure.json;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

@Component
public class RecordHttpServletRequestEntryReader implements RecordEntryReader<HttpServletRequest> {
    private final RecordEntryReader<InputStream> recordEntryReader;

    public RecordHttpServletRequestEntryReader(@Qualifier("recordEntryJsonReader") RecordEntryReader<InputStream> recordEntryReader) {
        this.recordEntryReader = recordEntryReader;
    }

    @Override
    public Stream<RecordEntry> read(HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            return recordEntryReader.read(is)
                    .onClose(() -> {
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
}
