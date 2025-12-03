package com.github.omaru.transaction.validator.infrastructure.json;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecordHttpServletRequestEntryReaderTest {
    @Mock
    private RecordEntryReader<InputStream> recordEntryReader;

    private RecordEntryReader<HttpServletRequest> reader;

    @BeforeEach
    void setUp() {
        reader = new RecordHttpServletRequestEntryReader(recordEntryReader);
    }

    @Test
    void shouldReadFromHttpServletRequest() {
        var request = new MockHttpServletRequest();

        reader.read(request);

        verify(recordEntryReader).read(any(InputStream.class));
    }
}
