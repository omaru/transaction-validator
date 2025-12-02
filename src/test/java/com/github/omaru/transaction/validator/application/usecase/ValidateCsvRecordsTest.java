package com.github.omaru.transaction.validator.application.usecase;

import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.Reason;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.service.RecordService;
import com.github.omaru.transaction.validator.domain.service.RecordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.stream.Stream;

import static com.github.omaru.transaction.validator.domain.service.RecordValidator.VALID_RECORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateCsvRecordsTest {
    @Mock
    private RecordEntryReader<InputStream> recordEntryReader;
    @Mock
    private RecordValidator recordValidator;
    @Mock
    private RecordService recordService;

    private ValidateCsvRecords useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateCsvRecords(recordEntryReader, recordValidator, recordService);
    }

    @Test
    void shouldReturnNoErrorsWhenAllValid() {
        var r1 = mock(RecordEntry.class);
        var r2 = mock(RecordEntry.class);

        when(recordEntryReader.read(any(InputStream.class))).thenReturn(Stream.of(r1, r2));
        when(recordValidator.validateEndBalance(any())).thenReturn(VALID_RECORD);
        when(recordValidator.validateUniqueTransactionReference(any())).thenReturn(VALID_RECORD);

        var report = useCase.execute(InputStream.nullInputStream());

        assertThat(report.getTotalRecordsRead()).isEqualTo(2L);
        assertThat(report.getFailedRecords()).isEmpty();

        verify(recordService).deleteAll();

    }

    @Test
    void shouldReturnReportWithErrors() {
        var r1 = mock(RecordEntry.class);
        var r2 = mock(RecordEntry.class);

        when(recordEntryReader.read(any(InputStream.class))).thenReturn(Stream.of(r1, r2));
        when(recordValidator.validateEndBalance(r1)).thenReturn(FailedRecord.builder().record(r1)
                .reason(Reason.INCORRECT_END_BALANCE).build());
        when(recordValidator.validateUniqueTransactionReference(r1)).thenReturn(FailedRecord.builder().record(r1)
                .reason(Reason.DUPLICATE_REFERENCE).build());
        when(recordValidator.validateEndBalance(r2)).thenReturn(VALID_RECORD);
        when(recordValidator.validateUniqueTransactionReference(r2)).thenReturn(VALID_RECORD);

        var report = useCase.execute(InputStream.nullInputStream());

        assertThat(report.getTotalRecordsRead()).isEqualTo(2L);
        assertThat(report.getFailedRecords()).hasSize(1);

        var failedRecord = report.getFailedRecords().getFirst();

        assertThat(failedRecord.getReasons()).containsExactlyInAnyOrder(Reason.INCORRECT_END_BALANCE, Reason.DUPLICATE_REFERENCE);

        verify(recordService).deleteAll();
    }

}
