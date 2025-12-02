package com.github.omaru.transaction.validator.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Report {
    private Long totalRecordsRead;
    private Long totalFailedRecords;
    private List<FailedRecord> failedRecords;
}
