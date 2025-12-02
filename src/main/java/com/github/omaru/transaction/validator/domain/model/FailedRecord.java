package com.github.omaru.transaction.validator.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class FailedRecord {
    private RecordEntry record;
    @Singular("reason")
    private List<Reason> reasons;
}
