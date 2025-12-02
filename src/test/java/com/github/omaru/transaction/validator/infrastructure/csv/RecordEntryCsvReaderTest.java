package com.github.omaru.transaction.validator.infrastructure.csv;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RecordEntryCsvReaderTest {
    private static final String TEST_CSV_PATH = "/records.csv";
    private RecordEntryReader<InputStream> reader;

    @BeforeEach
    void setUp() {
        reader = new RecordCsvEntryReader(new RecordEntryCsvMapper());
    }

    @Test
    void shouldReadFromCsv() throws IOException {
        try (final InputStream is = getClass().getResourceAsStream(TEST_CSV_PATH)) {
            assertNotNull(is, "Csv not found at" + TEST_CSV_PATH);
            final Stream<RecordEntry> records = reader.read(is);
            try (records) {
                var recordsList = records.toList();
                var recordEntry = recordsList.getFirst();
                assertThat(recordEntry.getTransactionReference()).isEqualTo(194261L);
                assertThat(recordEntry.getAccountNumber()).isEqualTo(Iban.valueOf("NL91RABO0315273637"));
                assertThat(recordEntry.getDescription()).isEqualTo("Book John Smith");
                assertThat(recordEntry.getStartBalance()).isEqualTo(BigDecimal.valueOf(21.6));
                assertThat(recordEntry.getMutation()).isEqualTo(BigDecimal.valueOf(-41.83));
                assertThat(recordEntry.getEndBalance()).isEqualTo(BigDecimal.valueOf(-20.23));
                assertThat(recordsList.size()).isEqualTo(10);
            }
        }
    }
}
