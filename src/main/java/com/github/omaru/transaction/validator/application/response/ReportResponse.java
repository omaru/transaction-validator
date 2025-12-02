package com.github.omaru.transaction.validator.application.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportResponse {
    private Integer totalRecordsRead;
    private Integer totalFailedRecords;
    private List<FailedRecordResponse> failedRecords;
}
