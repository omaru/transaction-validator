package com.github.omaru.transaction.validator.infrastructure.csv;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.application.usecase.exception.UnableToReadCsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordMultipartEntryReaderTest {
    @Mock
    private RecordEntryReader<InputStream> recordEntryCsvReader;

    private RecordEntryReader<MultipartFile> reader;

    @BeforeEach
    void setUp() {
        reader = new RecordMultipartEntryReader(recordEntryCsvReader);
    }

    @Test
    void shouldThrowExceptionWhenFileIsNotCsv() {
        var mockFile = mock(MultipartFile.class);

        when(mockFile.getContentType()).thenReturn("text/not-csv");

        var exception = assertThrows(UnableToReadCsvException.class, () -> reader.read(mockFile));

        assertThat(exception.getMessage()).contains("File is not a valid CSV");

    }

    @Test
    void shouldThrowExceptionWhenFileIsNotCsvFromFileExtension() {
        var mockFile = mock(MultipartFile.class);

        when(mockFile.getContentType()).thenReturn(null);
        when(mockFile.getOriginalFilename()).thenReturn("file.txt");

        var exception = assertThrows(UnableToReadCsvException.class, () -> reader.read(mockFile));

        assertThat(exception.getMessage()).contains("File is not a valid CSV");

    }
}