package com.github.omaru.transaction.validator.application.usecase;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
class ValidateCsvRecordsTest {
    @Mock
    private RecordEntryReader<InputStream> recordEntryReader;
    private ValidateCsvRecords useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateCsvRecords(recordEntryReader);
    }

    @Test
    void shouldReturnError() {

    }

}
