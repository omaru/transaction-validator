package com.github.omaru.transaction.validator.domain.service;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.infrastructure.persistence.jpa.NotUniqueReferenceNumberException;

public interface RecordService {
    RecordEntry save(RecordEntry record) throws NotUniqueReferenceNumberException;
}
