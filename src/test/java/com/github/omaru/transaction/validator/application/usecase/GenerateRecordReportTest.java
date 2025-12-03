package com.github.omaru.transaction.validator.application.usecase;

import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.Reason;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.service.RecordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.stream.Stream;

import static com.github.omaru.transaction.validator.domain.service.RecordValidator.VALID_RECORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateRecordReportTest {
    @Mock
    private RecordValidator recordValidator;

    private GenerateRecordReport useCase;

    @BeforeEach
    void setUp() {
        useCase = new GenerateRecordReport(recordValidator);
    }

    @Test
    void shouldReturnNoErrorsWhenAllValid() {
        var r1 = mock(RecordEntry.class);
        var r2 = mock(RecordEntry.class);

        when(recordValidator.validate(any(), any())).thenReturn(VALID_RECORD);

        var report = useCase.execute(Stream.of(r1, r2));

        assertThat(report.getTotalRecordsRead()).isEqualTo(2L);
        assertThat(report.getFailedRecords()).isEmpty();

    }

    @Test
    void shouldReturnReportWithErrors() {
        var r1 = mock(RecordEntry.class);
        var r2 = mock(RecordEntry.class);
        var seenReferences = new HashSet<Long>();

        when(recordValidator.validate(r1, seenReferences)).thenReturn(FailedRecord.builder().record(r1)
                .reason(Reason.INCORRECT_END_BALANCE)
                .reason(Reason.DUPLICATE_REFERENCE).build());
        when(recordValidator.validate(r2, seenReferences)).thenReturn(VALID_RECORD);

        var report = useCase.execute(Stream.of(r1, r2));

        assertThat(report.getTotalRecordsRead()).isEqualTo(2L);
        assertThat(report.getFailedRecords()).hasSize(1);

        var failedRecord = report.getFailedRecords().getFirst();

        assertThat(failedRecord.getReasons()).containsExactlyInAnyOrder(Reason.INCORRECT_END_BALANCE, Reason.DUPLICATE_REFERENCE);

    }

}
