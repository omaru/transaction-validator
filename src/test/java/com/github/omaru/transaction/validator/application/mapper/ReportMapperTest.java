package com.github.omaru.transaction.validator.application.mapper;


import com.github.omaru.transaction.validator.application.response.FailedRecordResponse;
import com.github.omaru.transaction.validator.application.response.RecordResponse;
import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.model.Report;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportMapperTest {
    private ReportMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReportMapper();
    }

    @Test
    void toResponse_mapsTotalsAndEmptyFailedRecords() {
        Report report = mock(Report.class);

        when(report.getTotalRecordsRead()).thenReturn(5);
        when(report.getTotalFailedRecords()).thenReturn(0);
        when(report.getFailedRecords()).thenReturn(Collections.emptyList());

        var response = mapper.toResponse(report);

        assertThat(response.getTotalRecordsRead()).isEqualTo(5);
        assertThat(response.getTotalFailedRecords()).isEqualTo(0);
        assertThat(response.getFailedRecords()).isNotNull();

    }

    @Test
    void toResponse_mapsFailedRecordAndRecordFields() {
        Report report = mock(Report.class);
        FailedRecord failedRecord = mock(FailedRecord.class);
        RecordEntry record = mock(RecordEntry.class);
        Iban iban = mock(Iban.class);

        when(iban.toString()).thenReturn("NL00TESTIBAN00000000");
        when(record.getTransactionReference()).thenReturn(123L);
        when(record.getAccountNumber()).thenReturn(iban);
        when(record.getDescription()).thenReturn("Payment");
        when(record.getStartBalance()).thenReturn(new BigDecimal("100.00"));
        when(record.getMutation()).thenReturn(new BigDecimal("-20.00"));
        when(record.getEndBalance()).thenReturn(new BigDecimal("80.00"));

        when(failedRecord.getRecord()).thenReturn(record);

        when(failedRecord.getReasons()).thenReturn((List) Collections.singletonList("INCORRECT_END_BALANCE"));

        when(report.getTotalRecordsRead()).thenReturn(1);
        when(report.getTotalFailedRecords()).thenReturn(1);
        when(report.getFailedRecords()).thenReturn(Collections.singletonList(failedRecord));

        var response = mapper.toResponse(report);

        assertThat(response.getTotalRecordsRead()).isEqualTo(1);
        assertThat(response.getTotalFailedRecords()).isEqualTo(1);
        assertThat(response.getFailedRecords().size()).isEqualTo(1);

        FailedRecordResponse fr = response.getFailedRecords().get(0);
        assertNotNull(fr);
        RecordResponse rr = fr.getRecord();
        assertNotNull(rr);

        assertThat(rr.getTransactionReference()).isEqualTo(123L);
        assertThat(rr.getAccountNumber()).isEqualTo("NL00TESTIBAN00000000");
        assertThat(rr.getDescription()).isEqualTo("Payment");
        assertThat(rr.getStartBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(rr.getMutation()).isEqualByComparingTo(new BigDecimal("-20.00"));
        assertThat(rr.getEndBalance()).isEqualByComparingTo(new BigDecimal("80.00"));

        assertThat(fr.getReasons()).isNotNull();
        assertThat(fr.getReasons().size()).isEqualTo(1);

    }
}