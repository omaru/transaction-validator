package com.github.omaru.transaction.validator.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
public class FailedRecord {
    private RecordEntry record;
    @Singular("reason")
    private List<Reason> reasons;
}
