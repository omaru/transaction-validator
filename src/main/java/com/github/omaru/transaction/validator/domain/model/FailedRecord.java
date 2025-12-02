package com.github.omaru.transaction.validator.domain.model;

import java.util.List;

public class FailedRecord {
    private RecordEntry record;
    private List<Reason> reason;
}
