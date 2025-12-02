package com.github.omaru.transaction.validator.application.mapper;

import com.github.omaru.transaction.validator.application.response.FailedRecordResponse;
import com.github.omaru.transaction.validator.application.response.RecordResponse;
import com.github.omaru.transaction.validator.application.response.ReportResponse;
import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.model.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportResponse toResponse(Report report) {
        return ReportResponse.builder()
                .totalRecordsRead(report.getTotalRecordsRead())
                .totalFailedRecords(report.getTotalFailedRecords())
                .failedRecords(report.getFailedRecords().stream().map(this::toFailedRecordResponse).toList())
                .build();
    }

    private FailedRecordResponse toFailedRecordResponse(FailedRecord failedRecord) {
        return FailedRecordResponse.builder()
                .record(toRecordResponse(failedRecord.getRecord()))
                .reasons(failedRecord.getReasons())
                .build();
    }

    private RecordResponse toRecordResponse(RecordEntry record) {
        return RecordResponse.builder()
                .transactionReference(record.getTransactionReference())
                .accountNumber(record.getAccountNumber().toString())
                .description(record.getDescription())
                .startBalance(record.getStartBalance())
                .mutation(record.getMutation())
                .endBalance(record.getEndBalance())
                .build();
    }
}