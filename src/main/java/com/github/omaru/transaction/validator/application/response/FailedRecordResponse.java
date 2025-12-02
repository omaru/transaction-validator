package com.github.omaru.transaction.validator.application.response;

import com.github.omaru.transaction.validator.domain.model.Reason;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FailedRecordResponse {
    private RecordResponse record;
    private List<Reason> reasons;
}
